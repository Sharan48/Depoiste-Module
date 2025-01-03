package com.scm2.servicesimp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.scm2.dto.UserDto;
import com.scm2.entity.User;
import com.scm2.repositaries.UserRepository;
import com.scm2.service.ExcelImportService;

@Service
public class ExcelImportServiceImp implements ExcelImportService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;

    private static final Pattern PHONE_REGEX = Pattern.compile("^\\d{10}$");

    @Override
    public String parseExcelFile(MultipartFile file, String filePath) throws IOException {

        List<UserDto> list = new ArrayList<>();
        boolean hasErrors = false;

        try (InputStream inputstream = file.getInputStream()) {
            Workbook workBook = WorkbookFactory.create(inputstream);
            // Workbook workbook = new XSSFWorkbook(inputstream);
            Sheet sheet = workBook.getSheetAt(0);

            int rowNub = 0;
            Iterator<Row> iterator = sheet.iterator();

            while (iterator.hasNext()) {
                Row row = iterator.next();

                // skip header row
                if (rowNub == 0) {
                    rowNub++;
                    continue;
                }

                // Break the loop if the row is empty
                if (isEmptyRow(row)) {
                    break;
                }

                String username = getcellValue(row.getCell(0));
                String email = getcellValue(row.getCell(1));
                String phonenumber = getcellValue(row.getCell(2));
                String password = getcellValue(row.getCell(3));

                if (userRepository.existsByUsername(username)) {
                    hasErrors = true;
                    Cell errorcell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
                    errorcell.setCellValue("Username already exists");
                } else if (userRepository.existsByphonenumber(phonenumber)) {
                    hasErrors = true;
                    Cell errorcell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
                    errorcell.setCellValue("Phonenumber already exists");
                } else if (userRepository.existsByEmail(email)) {
                    hasErrors = true;
                    Cell errorcell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
                    errorcell.setCellValue("Email already exists");
                }

                String errormessage = null;
                if (email == null || email.isEmpty() || !email.contains("@")) {
                    errormessage = "Email is not a valid email";
                } else if (phonenumber == null || phonenumber.isEmpty()
                        || !PHONE_REGEX.matcher(phonenumber).matches()) {
                    errormessage = "Phone number is not a valid phone number";
                }

                if (errormessage == null) {
                    UserDto userDto = new UserDto();
                    userDto.setUsername(username);
                    userDto.setEmail(email);
                    userDto.setPhonenumber(phonenumber);
                    userDto.setPassword(password);

                    list.add(userDto);
                } else {
                    // Add error message in the last cell of the row
                    hasErrors = true;
                    Cell errorcell = row.createCell(row.getLastCellNum() == -1 ? 0 : row.getLastCellNum());
                    errorcell.setCellValue(errormessage);
                }

            }
            // Save valid user in database
            List<User> users = list.stream().map((user) -> modelMapper.map(user, User.class))
                    .collect(Collectors.toList());
            userRepository.saveAll(users);

            // Save the updated Excel file with errors

            if (hasErrors) {

                try (FileOutputStream output = new FileOutputStream(filePath)) {
                    workBook.write(output);
                }
                workBook.close();

            }
            workBook.close();
            return filePath;

        }

    }

    @Override
    public boolean isRowValid(Row row, int rowIndex) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isRowValid'");
    }

    public String getcellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;

        }

    }

    public boolean isEmptyRow(Row row) {
        for (Cell cell : row) {
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true; // Row is completely empty
    }
}
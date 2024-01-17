package com.phuonghn.pkm;

import com.phuonghn.pkm.repository.PokemonRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@Slf4j
public class PkmApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(PkmApplication.class);
        Environment env = app.run(args).getEnvironment();
        logApplicationStartup(env);
    }

    private static void logApplicationStartup(Environment env) {
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (StringUtils.isBlank(contextPath)) {
            contextPath = "/";
        }
        String hostAddress = "localhost";
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.warn("The host name could not be determined, using localhost as fallback");
        }
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application '{}' is running! Access URLs:\n\t" +
                        "Local: \t\t{}://localhost:{}{}\n\t" +
                        "server: \t{}://{}:{}{}\n\t" +
                        "swagger: \t{}://localhost:{}{}swagger-ui.html\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                protocol,
                serverPort,
                contextPath,
                protocol,
                hostAddress,
                serverPort,
                contextPath,
                protocol,
                serverPort,
                contextPath,
                env.getActiveProfiles());
    }

    @Bean
    public CommandLineRunner loadData(PokemonRepo pokemonRepo) {
        return (args) -> {
            try {
            } catch (Exception e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        };
    }

//    @Test
//    public void genReceipt() {
//        try (FileInputStream file = new FileInputStream("F:/oldData.xlsx")) {
//            Workbook workbook = WorkbookFactory.create(file);
//            Sheet sheet = workbook.getSheetAt(0); // Assuming you want to read from the first sheet
//            int i = 0;
//            List<ReceiptDTO> receiptDTOS = new ArrayList<>();
//            for (Row row : sheet) {
//                if (i == 0) {
//                    i++;
//                    continue;
//                }
//                ReceiptDTO receiptDTO = new ReceiptDTO();
//                receiptDTO.setSubscriberId((long) row.getCell(5).getNumericCellValue());
//                receiptDTO.setCustomerOrderId((long) row.getCell(8).getNumericCellValue());
//                receiptDTO.setProductOfferId((long) row.getCell(7).getNumericCellValue());
//                receiptDTO.setPlanId(row.getCell(9).getStringCellValue());
//                receiptDTO.setEffectiveDate(row.getCell(0).getDateCellValue());
//                receiptDTO.setExpireDate(row.getCell(1).getDateCellValue());
//                receiptDTO.setTotal((long) row.getCell(2).getNumericCellValue());
//                receiptDTO.setStatus(row.getCell(3).getStringCellValue());
//                if (row.getCell(4).getCellType().equals(NUMERIC)) {
//                    receiptDTO.setTransCode(String.valueOf(row.getCell(4).getNumericCellValue()));
//                } else {
//                    receiptDTO.setTransCode(!isNullOrEmpty(String.valueOf(row.getCell(4).getStringCellValue())) ? String.valueOf(row.getCell(4).getStringCellValue()) : null);
//                }
//                receiptDTO.setNote(row.getCell(10) != null);
//                receiptDTOS.add(receiptDTO);
//            }
//
//            StringBuilder sqlReceipt = new StringBuilder();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//
//            // xử lý bản ghi có note
//            Map<Long, List<ReceiptDTO>> groupedByOrderId = receiptDTOS.stream()
//                    .collect(Collectors.groupingBy(ReceiptDTO::getCustomerOrderId));
//            for (Long orderId : groupedByOrderId.keySet()) {
//                List<ReceiptDTO> lstByOrderId = groupedByOrderId.get(orderId);
//                Map<String, List<ReceiptDTO>> groupedByPlanId = lstByOrderId.stream()
//                        .collect(Collectors.groupingBy(ReceiptDTO::getPlanId));
//                for (String planId : groupedByPlanId.keySet()) {
//                    List<String> idRandoms = new ArrayList<>();
//                    List<ReceiptDTO> rcs = groupedByPlanId.get(planId);
//                    ReceiptDTO receiptDTO = rcs.get(0);
//                    sqlReceipt.append("INSERT INTO RECEIPT_TEMP (SUBSCRIBER_ID,CREATED_DATE,CREATED_USER,PRODUCT_OFFER_ID,PLAN_ID,CUSTOMER_ORDER_ID) VALUES ");
//                    sqlReceipt.append("(").append(receiptDTO.getSubscriberId()).append(",");
//                    sqlReceipt.append("NOW()").append(",");
//                    sqlReceipt.append(toParamSQL("ADMIN")).append(",");
//                    sqlReceipt.append(receiptDTO.getProductOfferId()).append(",");
//                    sqlReceipt.append(toParamSQL(receiptDTO.getPlanId())).append(",");
//                    sqlReceipt.append(receiptDTO.getCustomerOrderId()).append(");\n");
//                    for (ReceiptDTO rDto : rcs) {
//                        String idRandom = rDto.getCustomerOrderId().toString() + genUniqueCodeFourChar(idRandoms);
//                        idRandoms.add(idRandom);
//                        sqlReceipt.append("INSERT INTO RECEIPT_TEMP_DETAIL (RECEIPT_TEMP_DETAIL_CODE,RECEIPT_TEMP_ID,TRANS_STATUS,EFFECTIVE_DATE,EXPIRE_DATE,");
//                        sqlReceipt.append("TOTAL_AMOUNT_VAT,BCCS_TRANS_ID,APPROVE_DATE,APPROVE_USER,DISCOUNT,");
//                        sqlReceipt.append("AMOUNT_EACH_STORAGE,TOTAL_DISCOUNT) ");
//                        sqlReceipt.append("VALUES (").append(idRandom).append(",");
//                        sqlReceipt.append("(select RECEIPT_TEMP_ID from RECEIPT_TEMP where PLAN_ID = ").append(toParamSQL(rDto.getPlanId())).append(")").append(",");
//                        // transaction status
//                        if (rDto.getNote()) {
//                            sqlReceipt.append("null");
//                        } else {
//                            if (rDto.getStatus().contains("Đã")) {
//                                sqlReceipt.append("2");
//                            } else {
//                                sqlReceipt.append("0");
//                            }
//                        }
//                        sqlReceipt.append(",");
//                        sqlReceipt.append(toParamSQL(sdf.format(rDto.getEffectiveDate()))).append(",");
//                        sqlReceipt.append(toParamSQL(sdf.format(rDto.getExpireDate()))).append(",");
//                        sqlReceipt.append(rDto.getTotal()).append(",");
//                        sqlReceipt.append(!isNullOrEmpty(rDto.getTransCode()) ? toParamSQL(rDto.getTransCode()) : "null").append(",");
//                        if (rDto.getStatus().contains("Đã")) {
//                            sqlReceipt.append("NOW()").append(",");
//                            sqlReceipt.append(toParamSQL("ADMIN")).append(",");
//                        } else {
//                            sqlReceipt.append("null").append(",");
//                            sqlReceipt.append("null").append(",");
//                        }
//                        sqlReceipt.append("0").append(",");
//                        if (groupedByPlanId.size() > 1) {
//                            sqlReceipt.append(rDto.getTotal() / groupedByPlanId.size());
//                        } else {
//                            sqlReceipt.append(rDto.getTotal());
//                        }
//                        sqlReceipt.append(",");
//                        sqlReceipt.append("0");
//                        sqlReceipt.append(");\n");
//                    }
//                    sqlReceipt.append("\n\n");
//                }
//            }
//            System.out.println("");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public String toParamSQL(String s) {
        return "'" + s + "'";
    }

    public String genUniqueCodeFourChar(List<String> idRandoms) {
        Random random = new Random();
        String idRandom = String.format("%04d", random.nextInt(10000));
        String finalIdRandom = idRandom;
        if (idRandoms.stream().anyMatch(e -> e.equals(finalIdRandom))) {
            idRandom = genUniqueCodeFourChar(idRandoms);
        }
        idRandoms.add(idRandom);
        return idRandom;
    }

    public static boolean isNullOrEmpty(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}

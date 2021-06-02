package ecma.ai.ussdapp.service;

import ecma.ai.ussdapp.component.DetailExporter;
import ecma.ai.ussdapp.entity.Details;
import ecma.ai.ussdapp.entity.SimCard;
import ecma.ai.ussdapp.entity.enums.ActionType;
import ecma.ai.ussdapp.payload.ApiResponse;
import ecma.ai.ussdapp.payload.DetailDto;
import ecma.ai.ussdapp.repository.DetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DetailsService {
    @Autowired
    DetailsRepository detailsRepository;
    @Autowired
    DetailExporter detailExporter;


    public ApiResponse add(DetailDto detailDto) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (simCard.getId().toString().equals(detailDto.getSimCard().getId().toString())) {
            Details details = new Details();
            details.setAmount(detailDto.getAmount());
            details.setActionType(detailDto.getActionType());
            details.setSimCard(detailDto.getSimCard());
            detailsRepository.save(details);
            return new ApiResponse("Details sqlandi!", true);
        }
        return new ApiResponse("Error!", false);
    }

    public ApiResponse exportToPdf(HttpServletResponse response, List<Details> details, String code) throws IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        String header = "Content_Disposition";
        String headerValue = "attchment; filename=detail_" + currentTime + ".pdf";

        response.setHeader(header, headerValue);

        detailExporter.exportPDF(response, details, code);


        return new ApiResponse("Successfully!", true);
    }

    public ApiResponse exportToExcel(HttpServletResponse response, List<Details> detailsList) throws IOException {
        response.setContentType("application/octet-stream");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentTime = dateFormat.format(new Date());

        String headerKey = "Content-Disposition";
        String headervalue = "attchmentl filename=user_" + currentTime + ".xlsx";

        response.setHeader(headerKey, headervalue);
        detailExporter.exportExcel(response, detailsList);
        return new ApiResponse("Successfully!", true);
    }

    public ApiResponse getAllSimCard(HttpServletResponse response, Integer stat) throws IOException {
        SimCard card = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Details> detailsList = detailsRepository.findAllBySimCard(card);
        if (stat == 0) {
            return exportToPdf(response, detailsList, card.getCode());
        } else if (stat == 1) {
            return exportToExcel(response, detailsList);
        } else if (stat == 2) {
            return new ApiResponse("List for details", true, detailsList);
        } else {
            return new ApiResponse("Error!", false);
        }

    }

    public ApiResponse getAllBySimCardActionType(HttpServletResponse response, String action, Integer stat) throws IOException {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ActionType actionType = null;
        for (ActionType value : ActionType.values()) {
            if (value.toString().equalsIgnoreCase(action)) {
                actionType = value;
                break;

            }
        }

        if (actionType == null) {
            return new ApiResponse("Error action type!", false);
        }
        List<Details> detailsList = detailsRepository.findAllByActionTypeAndSimCard(actionType, simCard);
        if (stat == 0) {
            return exportToPdf(response, detailsList, simCard.getCode());
        } else if (stat == 1) {
            return exportToExcel(response, detailsList);
        } else if (stat == 2) {
            return new ApiResponse("List for details", true, detailsList);
        } else {
            return new ApiResponse("Error!", false);
        }


    }


}

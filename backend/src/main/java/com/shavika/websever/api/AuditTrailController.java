package com.shavika.websever.api;


import com.shavika.websever.api.webmodels.ErrorResponse;
import com.shavika.websever.services.AuditTrailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/audit")
public class AuditTrailController {

    private AuditTrailService auditTrailService;

    // Endpoint for getting paginated audit trail data
    @GetMapping
    public ResponseEntity<?> getAuditTrails() {
        try {
            return new ResponseEntity<>(auditTrailService.getAuditTrails(), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{traceid}")
    public ResponseEntity<?> getAuditTrailsDetails(@PathVariable("traceid") String traceId) {
        try {
            return new ResponseEntity<>(auditTrailService.getAuditTrailsSpanDetails(traceId), HttpStatus.OK);
        } catch (Exception e) {
            // Handle exceptions and return error response
            e.printStackTrace();
            ErrorResponse response = ErrorResponse.builder()
                    .message(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

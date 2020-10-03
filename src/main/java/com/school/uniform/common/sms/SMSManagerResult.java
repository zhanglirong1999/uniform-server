package com.school.uniform.common.sms;

import com.school.uniform.exception.BizException;
import com.school.uniform.util.ConstantUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SMSManagerResult {

    private List<OneSMSResult> successfulResults = new LinkedList<>();

    private List<OneSMSResult> failedResults = new LinkedList<>();

    public void addOneSMSResult(OneSMSResult result) {
        if (result.isSuccessful()) {
            successfulResults.add(result);
        } else {
            failedResults.add(result);
        }
    }

    public OneSMSResult resolveOneSMSResult()  {
        List<OneSMSResult> merged = new LinkedList<>(successfulResults);
        merged.addAll(failedResults);
        if (merged.size() != 1) {
            throw new BizException(ConstantUtil.BizExceptionCause.ERROR_PHONE);
        }
        return merged.get(0);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OneSMSResult {

        public static final String SUCCESSFUL_STATUS = "Ok";

        private String phone;
        private String verifyCode;
        private String status;
        private String message;

        public boolean isSuccessful() {
            return status.equalsIgnoreCase(SUCCESSFUL_STATUS);
        }
    }
}


package com.ansh.request;


import com.ansh.enums.ChannelType;
import com.ansh.enums.ErrorCode;
import com.ansh.exceptions.CommunicationException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SMSRequest extends CommunicationRequest {
    private String mobileNumber;
    private String message;

    @Override
    public ChannelType getChannelType() {
        return ChannelType.SMS;
    }

    @Override
    public void validate() {
        if (mobileNumber == null || mobileNumber.isBlank() || message == null || message.isBlank()) {
            throw new CommunicationException(ErrorCode.INVALID_REQUEST, "SMS request requires mobileNumber and message.");
        }
    }
}

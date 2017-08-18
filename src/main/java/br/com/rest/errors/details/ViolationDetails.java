package br.com.rest.errors.details;

public class ViolationDetails extends ErrorDefaultDetails{

    private ViolationDetails() {}       
    
    public static final class Builder {
        private String title;
        private int status;
        private String detail;
        private long timestamp;
        private String developerMessage;

        public static Builder newBuilder() {
            return new Builder();
        }        
        
        private Builder() {
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
            return this;
        }

        public Builder detail(String detail) {
            this.detail = detail;
            return this;
        }

        public Builder timestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder developerMessage(String developerMessage) {
            this.developerMessage = developerMessage;
            return this;
        }

        public ViolationDetails build(){
            ViolationDetails constraintViolationDetails = new ViolationDetails();
            constraintViolationDetails.setTitle(this.title);
            constraintViolationDetails.setStatus(this.status);
            constraintViolationDetails.setDetail(this.detail);
            constraintViolationDetails.setTimestamp(this.timestamp);
            constraintViolationDetails.setDeveloperMessage(developerMessage);
            return constraintViolationDetails;
        }
    }
    
}

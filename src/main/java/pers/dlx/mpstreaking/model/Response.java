package pers.dlx.mpstreaking.model;

import lombok.Data;

@Data
public class Response<T> {
    private T content;

    private RequestResult result;

    public Response() {
        result = new RequestResult();
        result.setSuccess(true);
    }
}
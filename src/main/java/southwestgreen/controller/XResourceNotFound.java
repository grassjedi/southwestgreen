package southwestgreen.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import southwestgreen.XNoSuchRecord;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class XResourceNotFound extends RuntimeException {
    public XResourceNotFound(String s, XNoSuchRecord e) {
        super(s, e);
    }
}

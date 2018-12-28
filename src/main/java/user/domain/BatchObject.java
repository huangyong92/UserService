package user.domain;

import com.gexin.rp.sdk.template.AbstractTemplate;

public class BatchObject {
    public AbstractTemplate template;
    public String clientId;

    public BatchObject(AbstractTemplate template, String clientId) {
        this.template = template;
        this.clientId = clientId;
    }
}

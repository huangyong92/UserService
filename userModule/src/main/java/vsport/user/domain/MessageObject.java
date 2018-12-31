package vsport.user.domain;

public class MessageObject<T> {

    public String methodType;

    public T obj;

    public MessageObject(String methodType, T obj) {
        this.methodType = methodType;
        this.obj = obj;
    }
}

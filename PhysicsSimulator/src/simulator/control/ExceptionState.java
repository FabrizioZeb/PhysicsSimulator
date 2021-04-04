package simulator.control;

public class ExceptionState extends Exception {

    public ExceptionState(){
        super();
    }

    public ExceptionState(String message){
        super(message);
    }

    public ExceptionState(String message, Throwable cause){
        super(message,cause);
    }

    public ExceptionState(Throwable cause){
        super(cause);
    }



}

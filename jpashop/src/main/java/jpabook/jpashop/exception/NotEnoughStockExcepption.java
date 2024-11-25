package jpabook.jpashop.exception;

public class NotEnoughStockExcepption extends RuntimeException {
    public NotEnoughStockExcepption() {
        super();
    }

    public NotEnoughStockExcepption(String message) {
        super(message);
    }

    public NotEnoughStockExcepption(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockExcepption(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockExcepption(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

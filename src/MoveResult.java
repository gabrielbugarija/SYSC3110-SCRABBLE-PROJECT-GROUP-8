import java.util.List;
//Gabriel Bugarija 101262776
public class MoveResult {

    private boolean success;
    private int earnedPoints;
    private List<String> formedWords;
    private String message;



    public boolean isSuccess() {
        return success;
    }

    public int getEarnedPoints() {
        return earnedPoints;
    }

    public List<String> getFormedWords() {
        return formedWords;
    }

    public String getMessage() {
        return message;
    }

}

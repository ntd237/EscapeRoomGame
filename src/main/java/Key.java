import java.awt.Color;

public class Key extends GameObject {
    private boolean collected;
    private Question question;

    public Key(int x, int y) {
        super(x, y, Constants.KEY_IMAGE, Color.YELLOW);
        this.collected = false;
        this.question = Question.getRandomQuestion();
    }

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
    }

    public Question getQuestion() {
        return question;
    }
}
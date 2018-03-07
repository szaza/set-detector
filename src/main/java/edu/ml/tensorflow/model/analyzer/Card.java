package edu.ml.tensorflow.model.analyzer;

import edu.ml.tensorflow.util.analyzer.CardUtil;
import java.util.Objects;

public class Card {
    /* 0-red, 1-green, 2-purple */
    private Integer color;
    /* 0-croissant, 1-diamond, 2-oval */
    private Integer shape;
    /* 0-blank, 1-striped, 2-filled*/
    private Integer fill;
    // Number of shapes
    private Integer number;

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public Integer getShape() {
        return shape;
    }

    public void setShape(Integer shape) {
        this.shape = shape;
    }

    public Integer getFill() {
        return fill;
    }

    public void setFill(Integer fill) {
        this.fill = fill;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getAttrByCategoryIndex(final int index) {
        switch (index) {
            case 0: return getNumber();
            case 1: return getColor();
            case 2: return getFill();
            case 3: return getShape();
            default: return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        Card other = (Card) obj;
        return this.getNumber().equals(other.getNumber())
                && this.getColor().equals(other.getColor())
                && this.getFill().equals(other.getFill())
                && this.getShape().equals(other.getShape());
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, shape, fill, number);
    }

    @Override
    public String toString() {
        return "Card{" + (this.getNumber()+1) +
                "-" + CardUtil.convertToColor(color) +
                "-" + CardUtil.convertToFill(fill) +
                "-" + CardUtil.convertToShape(shape) +
                '}';
    }
}

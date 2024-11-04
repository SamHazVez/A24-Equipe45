package Equipe45.domain.DTO;

public class DimensionDTO {
    public float width;
    public float height;

    @Override
    public String toString() {
        return "DimensionDTO{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }

    public DimensionDTO(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

}

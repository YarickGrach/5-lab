public record Order(int id, String shoeType, int quantity) {
    @Override
    public String toString() {
        return String.format("Order#%d: %s x%d", id, shoeType, quantity);
    }
}
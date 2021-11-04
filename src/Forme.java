ublic class Forme {
    
    Point point = new Point();
    
    public void getX() {
        getFoo();
        getBar();
        point.getY();
    }

    public int getBar() {
        getFoo();
        return 0;
    }

    public int getFoo() {
        return 1;
    };
    private int getBarBar() {
        return 0;
    }
}


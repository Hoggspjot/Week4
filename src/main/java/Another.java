public class Another {
    public static void main(String[] args) {





    }
}

class Eater {
    String name;
    Kitchen kitchen = new Kitchen();


    public Eater(String name) {
        this.name = name;
    }

    public void eating() {


        if (kitchen.hasKnife && kitchen.hasFork) {
            System.out.printf("%s вкусно покушал", name);
        }
        if (!kitchen.hasFork && kitchen.hasKnife) {
            System.out.printf("%s не хватает вилки, ");
        }

    }

}



class Kitchen {
    boolean hasFork = true;
    boolean hasKnife = true;

    public void takeFork() {
        hasFork = false;
    }

    public void takeKnife() {
        hasKnife = false;
    }

}

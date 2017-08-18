package tzz.customlistview_example;

/**
 * Created by tzz on 8/18/17.
 *
 */

 class Person {

    private String name, surname;

     Person(String n, String s){
        name=n;
        surname=s;
    }

    public String getName(){
        return name;
    }

     String getSurname(){
        return surname;
    }

}

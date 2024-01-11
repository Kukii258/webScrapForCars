package org.example;

import org.jsoup.select.Elements;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Scanner;
import java.util.ArrayList;



public class Oglas {

    Scanner scanner = new Scanner(System.in);
    public Elements cars = new Elements();
    String markaVozila;
    String url = "https://www.index.hr/oglasi/osobni-automobili/gid/27?pojam=&sortby=1&elementsNum=10&cijenaod=0&cijenado=47000&tipoglasa=1&markavozila="+markaVozila+"&pojamZup=-2&attr_Int_179=&attr_Int_1190=&attr_Int_470=&attr_Int_910=&attr_bit_motor=&attr_bit_473=&attr_Int_1172=&attr_Int_1335=&attr_Int_359=&attr_Int_1192=&attr_bit_klima=&attr_bit_mjenjac=&attr_bit_brzina=&attr_bit_vrsta_pogona=&vezani_na=179-1190_470-910_1172-1335_359-1192";

    Oglas(){}

    public void Trazilica() {

        int i = 1;
        String title = "", price = "";
        //brise listu cars da se ne nadopunjava nego da je "nova" lista
        cars.clear();

        try {
            //ucitava url i stavlja sve elemente s klasom ".OglasiRezHoledr" u Elements cars1
            Document document = Jsoup.connect(url).get();
            Elements cars1 = document.select(".OglasiRezHolder");

            //stavlja samo pune elemente u Elements car
            for (Element car : cars1) {
                if (!car.text().isEmpty()) {
                    cars.add(car);
                }
            }

            //uzima ime i cijenu automobila te printa
            for (Element bk : cars) {
                title = bk.select(".title").text();
                price = bk.select(".price").text();

                System.out.println(i + ". " + title + " - " + price);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        //poziva izbornik
        izbornik();
    }

    //Korisnik odabire koji auto zeli pretrazivati te se onda url mjenja
    public void odabirAuta(){
        System.out.println("Izaberi koji auto zelis pretrazivati: ");
        System.out.println("1. Renault \n2. Mercedes \n3. Volkswagen \n4. BMW \n5. Peugeot");
        int brojAuta =0;

        //while petlja koja omogucuje da se odaberu samo brojevi od 1 do 5, te lovi greske
        while(brojAuta < 1 || brojAuta > 5){
            try {
                System.out.print("Izaberi: ");
                brojAuta = scanner.nextInt();
                switch (brojAuta) {
                    case 1:
                        markaVozila = "11687";
                        break;
                    case 2:
                        markaVozila = "11481";
                        break;
                    case 3:
                        markaVozila = "11944";
                        break;
                    case 4:
                        markaVozila = "11005";
                        break;
                    case 5:
                        markaVozila = "11617";
                        break;
                    default:
                        System.out.println("Krivi unos");
                        scanner.nextLine();
                        break;
                }
            }catch (Exception e){
                System.out.println("Krivi unos!");
                scanner.nextLine();
            }
        }
        //postavlja url na marku vozila koju je korisnik gore odabrao
        url = "https://www.index.hr/oglasi/osobni-automobili/gid/27?pojam=&sortby=1&elementsNum=10&cijenaod=0&cijenado=47000&tipoglasa=1&markavozila="+markaVozila+"&pojamZup=-2&attr_Int_179=&attr_Int_1190=&attr_Int_470=&attr_Int_910=&attr_bit_motor=&attr_bit_473=&attr_Int_1172=&attr_Int_1335=&attr_Int_359=&attr_Int_1192=&attr_bit_klima=&attr_bit_mjenjac=&attr_bit_brzina=&attr_bit_vrsta_pogona=&vezani_na=179-1190_470-910_1172-1335_359-1192";
        Trazilica();
    }

    //Izbornik za mijenjanje stranica ili odabire auto
    //num = 1; da se ne resetira svaki put kat se funkcija izbornik() pokrene
    int num = 1;
    public void izbornik() {

        System.out.println("Izaberi koji auto: \n> za sljedecu stranicu\n< za prethodnu stranicu \nx ako zelis pretrazivati novi auto");
        String izborS ="";
        boolean success = false;

        //upozorava na greske tako dugo dok se ne unese prava vrijednost pomocu vrijednosti success
        do {
            try {
                System.out.print("Izaberi: ");
                izborS = scanner.next();
                //za mijenjanje stranice unaprijed
                if (izborS.equals(">")) {
                    num++;
                    url = "https://www.index.hr/oglasi/osobni-automobili/gid/27?pojamZup=-2&markavozila=" + markaVozila + "&tipoglasa=1&sortby=1&elementsNum=10&grad=0&naselje=0&cijenaod=0&cijenado=59339&vezani_na=179-1190_470-910_1172-1335_359-1192&num=" + num;
                    Trazilica();

                    //za mijenjanje stranice unazad
                } else if (izborS.equals("<")) {
                    num = (num < 2) ? num : --num;
                    url = "https://www.index.hr/oglasi/osobni-automobili/gid/27?pojamZup=-2&markavozila=" + markaVozila + "&tipoglasa=1&sortby=1&elementsNum=10&grad=0&naselje=0&cijenaod=0&cijenado=59339&vezani_na=179-1190_470-910_1172-1335_359-1192&num=" + num;
                    Trazilica();

                } else if (izborS.equals("x")) {
                    odabirAuta();
                }

                //ako smo odabrali auto onda ucitava stranicu toga auta
                else {
                    //uzima link od auta kojeg smo odabrali
                    String url1 = cars.get(Integer.parseInt(izborS) - 1).select(".result").attr("href");
                    odabirInfo(url1);
                }

                //ako nema errora success se postavlja na true i izlazi se iz petlje
                success = true;
            }catch (Exception e){
                System.out.println("Krivi unos!");
            }
        }while(!success);
    }
    //Izbornik o informacijama o odabranome autu
    public void odabirInfo(String url){

        int broj = 0;
        //uzima sve osnovne,opsirne,i opremu o automobilu
        try {
            Document document2 = Jsoup.connect(url).get();
            Elements osnovno = document2.select("li:nth-child(1) .features_list.oglasHolder_1 .features-wrapper ul  li");
            Elements opsirnije = document2.select("li:nth-child(2) .features_list.oglasHolder_1 .features-wrapper ul  li");
            Elements oprema = document2.select(".features_list.oglasHolder_2 .features-wrapper ul  li");

            String imeAuta = document2.select(".likeH1").text();

            //stavlja ih u listu
            ArrayList<Elements> lista = new ArrayList<>();
            lista.add(osnovno);
            lista.add(opsirnije);
            lista.add(oprema);

            System.out.println("Odabrali ste auto: " + imeAuta);
            System.out.println("Osnovno o autu - 1\nOpsirno o autu - 2\nOprema auta - 3\nIzlaz - 0");

            //while petlja koja daje da izaberemo sta zelimo i izlazi kada upisemo 0
            while (broj != -1) {
                try {
                    System.out.print("Izaberi: ");
                    broj = scanner.nextInt() - 1;

                    for (Element element : lista.get(broj)) {
                        String labela = element.text();  // Dohvatite tekst iz <li class="labela"> elementa
                        Element vrijednostElement = element.nextElementSibling();

                        if (vrijednostElement != null) {
                            String vrijednost = vrijednostElement.text();  // Dohvatite tekst iz sljedećeg <li> elementa
                            System.out.println(labela + ": " + vrijednost);
                        }
                    }
                    System.out.println("############################");
                    System.out.println("Osnovno o autu - 1\nOpsirno o autu - 2\nOprema auta - 3\nIzlaz - 0");
                } catch (Exception e) {
                    //izbaci krivi unos svima osim 0 odnosno -1
                    if(broj != -1)
                        System.out.println("Krivi unos!");
                    scanner.nextLine();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        System.out.println("Pokrecem trazilicu");
        Trazilica();
    }
}
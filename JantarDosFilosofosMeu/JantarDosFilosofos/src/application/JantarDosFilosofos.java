package application;

//import java.io.IOException;
import java.util.Random;



import application.AcaoFilosofo.Acao;

public class JantarDosFilosofos{

    //Declarando variaveis a serem utilizadas
    private Random random = new Random();            //Variavel aleatoria para gerar filosofo com fome
    private Filosofo[] filosofos = new Filosofo[5];  // Variavel do tipo filosofo para armazenar os 5 filosofos presentes nas funcções
    private int tempoAtual;                          //Variavel para demarcar o tempo atual em que se encontra a mesa
    private int x, y , z;                            //Variaveis genericas
    private double[] contaFomeLocal = new double[5]; //Variavel para contar quantas vezes cada filosofo ficou com fome
    private double fomeTotal = 0;                    //Variavel para somar o tempo com fome de todos os filosofos
    private double contaFomeTotal = 0;               //Variavel para somar todas as vezes que os filosofos ficarao com fome
    private boolean[] garfo = new boolean[5];        //Variavel que indica quais garfos estao disponiveis no estado em que se encontra na mesa
    private final int tempo = 180000;                 

    public JantarDosFilosofos(){
        
        System.out.println("\n Jantar dos Filosofos\n");
        
        preparaJantar();

        for(tempoAtual =1;tempoAtual<tempo;tempoAtual++){ //Execução principal do programa

            filosofoTerminaRefeicao();
            filosofosTentamComer();
            if(tempoAtual % 3 == 0){
                geraFilosofoComFome();
            }
            mostraMesa();
        }
        for(int i=0;i<5;i++){
            System.out.println("\nTempo médio que o " + filosofos[i].getNome()+ "demorou para comer (ficou com fome): "+ filosofos[i].getTempoComFome() / filosofos[i].getContaFome()+ "\n");
            fomeTotal = fomeTotal + filosofos[i].getTempoComFome();
            contaFomeTotal = contaFomeTotal + filosofos[i].getContaFome();
        }
        System.out.println("\nTempo médio para os filósofos comerem " + fomeTotal / contaFomeTotal + "\n");
}

    public int getTempoAtual(){
        return tempoAtual;
    }

    public void preparaJantar(){      //Instancia os filosos e coloca os garfos na mesa
        for (x=0; x<5; x++){
            filosofos[x] = new Filosofo(Acao.PENSANDO);
            garfo[x] = true;
            switch (x) {
                case 0:
                    filosofos[0].setNome("Aristoteles");
                    contaFomeLocal[0] =0;
                    break;
                case 1:
                    filosofos[1].setNome("Descartes");
                    contaFomeLocal[1] =0;
                    break;
                case 2:
                    filosofos[2].setNome("Pìtagoras");
                    contaFomeLocal[2] =0;
                    break;
                case 3:
                    filosofos[3].setNome("Platao");
                    contaFomeLocal[3] =0;
                    break;
                case 4:
                    filosofos[4].setNome("Socrates");
                    contaFomeLocal[4] =0;
                    break;
            }
        }
    }

    public void filosofoTerminaRefeicao(){
        for(y =0; y<5;y++){
            if(filosofos[y].getAcao() == Acao.COMENDO){   //Condicao para mostrar quais filosofos estao comendo
                filosofos[y].addTempoComendo();           //Aumenta o tempo que o filosofo passa comendo
                if(filosofos[y].getTempoComendo() ==5){
                    synchronized (filosofos[y]){
                        if(y ==4){
                            garfo[0] = true;
                        }else{
                            garfo[y+1] = true;
                        }
                        garfo[y] = true;
                        filosofos[y].setTempoComendo(0);
                        filosofos[y].setAcao(Acao.PENSANDO);
                        try {
                            Thread.sleep(50);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        filosofos[y].notifyAll();
                        filosofos[y].setSegurandoGarfo(false);
                    }
                }
            }

        }
    }

    public void geraFilosofoComFome(){ //Função para mudar ação de um filosofo de PENSANDO para COM FOME
        y=0;
        for(x=0;x<5;x++){
            if(filosofos[x].getAcao() != Acao.PENSANDO){ 
                y++;
            }
        }
        if(y != 5){
            z = random.nextInt(5);
            while((filosofos[z].getAcao() != Acao.PENSANDO)){
                z = random.nextInt(5);
            }
            filosofos[z].setAcao(Acao.COM_FOME);
            contaFomeLocal[z]++;
            filosofos[z].setContaFome(contaFomeLocal[z]);
        }
    }
    public void filosofosTentamComer(){
        for(y=0;y<5;y++){
           
            if(y==4){
                x =0;
            }else{
                x = y+1;
            }
            if(filosofos[y].getAcao() == Acao.COM_FOME){
                     if(filosofos[y].pegaGarfos(garfo[y], garfo[x])){  //Garante que os dois garfos ao lado do filosofo COM FOME estao disponivel
                        garfo[x] = false;
                      garfo[y] = false;
                    }
            }
    }
}

    public String temGarfo(boolean garfoString){
        String trocaString;
        if(garfoString == false){
            trocaString = "Sem garfo";
        }else {
            trocaString = "Com garfo";
        }
        return trocaString;
    }

    public void mostraMesa(){     //Menu princiapl que mostra o funcionamento da mesa dos filosofos

        System.out.println("\n\n\n -------------------------MESA---------------------------\n\n");

        System.out.println("\n                    "+ filosofos[0].getNome()+" "+ filosofos[0].getAcao());
        System.out.println("\n        1: "+ temGarfo(garfo[0]) + "                    2:" + temGarfo(garfo[1]));
        System.out.println("\n" + filosofos[4].getNome()+ " " + filosofos[4].getAcao() + "                        " + filosofos[1].getNome() + " " + filosofos[1].getAcao());
        System.out.println("\n      5: "+ temGarfo(garfo[4])+ "                  3: "+ temGarfo(garfo[2]));
        System.out.println("\n      " + filosofos[3].getNome() + " " + filosofos[3].getAcao()+ "             "+ filosofos[2].getNome() + " " + filosofos[2].getAcao());
        System.out.println("\n                       4: " + temGarfo(garfo[3]));
        System.out.println("\n\nTempo atual: "+ tempoAtual);
    }
    public static void main(String[] args){
        new JantarDosFilosofos();
    }

}
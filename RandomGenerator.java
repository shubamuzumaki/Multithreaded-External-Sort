import java.io.PrintWriter;
import java.util.Random;


class RandomGenerator
{
    static void generateRandomInt(String file,long bound)throws Exception
    {
        PrintWriter fout = new PrintWriter(file);

        Random rand = new Random();

        fout.print(Math.abs(rand.nextInt()));
        do 
        {
            fout.println();
            fout.print(Math.abs(rand.nextInt()));            
        } while ((--bound)>1);

        fout.flush();
        fout.close();
    }
}
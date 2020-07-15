import java.io.*;
import java.util.*;

class Putter implements Runnable
{
    ArrayList<Integer> numbers;
    File file;
    Putter(ArrayList<Integer> numbers, File file)
    {
        this.numbers = numbers;
        this.file = file;
    }

    private void  put() throws Exception
    {
        Collections.sort(numbers);
            
        Iterator<Integer> itr = numbers.iterator();
        if(!itr.hasNext()) return;

        PrintWriter pw = new PrintWriter(file);

        pw.print(itr.next());
        while(itr.hasNext())
        {
            pw.println();
            pw.print(itr.next());            
        }
        pw.flush();
        pw.close();
    }

    @Override
    public void run()
    {
        try 
        {
            put();
        } 
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }
}


class Sort
{
    static void ExternalSort(String src,String target,boolean fastSortEnabled)throws Exception
    {
        int chunkSize = 100000*90;//= calculateChunkSize(new File(src));//100000*20
        ArrayList<String> splittedFiles = null;

        if(fastSortEnabled)
            splittedFiles = fastSplitFile(src,chunkSize);
        else
            splittedFiles = splitFile(src, chunkSize);
        mergeFiles(splittedFiles, target);
    }

    private static int calculateChunkSize(File file)
    {
        long totalLength = file.length();
        //todo: imporve logic
        return (int)totalLength/25;
    }

    private static ArrayList<String> fastSplitFile(String src,int chunkSize)throws Exception
    {
        System.out.println("Fast Sort...");
        ArrayList<String> splittedFiles = new ArrayList<>();
        ArrayList<Thread> threadPool = new ArrayList<>();
        Scanner srcFile = new Scanner(new FileReader(src));

        int fileCount = 0;

        boolean hasReachedEnd = false;
        while(!hasReachedEnd)
        {
            var numbers = new ArrayList<Integer>();
            for(int j=0; j<chunkSize && srcFile.hasNext(); ++j)
                numbers.add(srcFile.nextInt());

            if(!srcFile.hasNext())
                hasReachedEnd = true;

            String tempFile = String.valueOf(fileCount++);
            Thread t = new Thread(new Putter(numbers, new File(tempFile)));
            t.start();

            threadPool.add(t);
            splittedFiles.add(tempFile);
        }
        
        srcFile.close();
        
        for(Thread t:threadPool)
            t.join();

        return splittedFiles;
    }


    private static ArrayList<String> splitFile(String src,int chunkSize)throws Exception
    {
        ArrayList<String> splittedFiles = new ArrayList<>();
        Scanner srcFile = new Scanner(new FileReader(src));
        
        int fileCount = 0;

        boolean hasReachedEnd = false;
        while(!hasReachedEnd)
        {
            var numbers = new ArrayList<Integer>();
            for(int j=0; j<chunkSize && srcFile.hasNext(); ++j)
                numbers.add(srcFile.nextInt());

            if(!srcFile.hasNext())
                hasReachedEnd = true;

            Collections.sort(numbers);
            
            Iterator<Integer> itr = numbers.iterator();
            if(!itr.hasNext()) continue;

            File file = new File(String.valueOf(fileCount++));
            splittedFiles.add(file.toString());
            PrintWriter pw = new PrintWriter(file);

            pw.print(itr.next());
            while(itr.hasNext())
            {
                pw.println();
                pw.print(itr.next());            
            }
            pw.flush();
            pw.close();
        }

        srcFile.close();
        return splittedFiles;
    }

    private static void mergeFiles(ArrayList<String> fileNameList , String target)throws Exception
    {
        int srcFileLen = fileNameList.size();
        Scanner[] srcFiles = new Scanner[srcFileLen];
        PrintWriter out = null;

        try 
        {
            for(int i=0; i<srcFileLen; ++i)
                srcFiles[i] = new Scanner(new FileReader(fileNameList.get(i)));
    
            out = new PrintWriter(target);

            PriorityQueue<Pair> pq = new PriorityQueue<>(Pair.getComparator());

            for(int i=0; i<srcFileLen; ++i)
                if(srcFiles[i].hasNextInt())
                    pq.add(new Pair(srcFiles[i].nextInt(),i));
            
            boolean isFirstEntry = true;
            while(!pq.isEmpty())
            {
                Pair p = pq.remove();
                
                if(srcFiles[p.getInd()].hasNextInt())
                    pq.add(new Pair(srcFiles[p.getInd()].nextInt(),p.getInd()));
                
                if(isFirstEntry)
                {
                    out.print(p.getValue());
                    isFirstEntry = false;
                    continue;
                }
                out.println();
                out.print(p.getValue());
            }
        } 
        finally
        {
            //close resources
            for(int i=0; i<srcFileLen; ++i)
            {
                srcFiles[i].close();
                new File(fileNameList.get(i)).delete();
            }
            out.close();
        }
    } 
}

class Pair
{
    private int value;
    private int ind;
    private static Comparator<Pair> comparator = new Comparator<Pair>() 
    {
        @Override
        public int compare(Pair a, Pair b)
        {
            if(a.value < b.value)  return -1;
            if(a.value > b.value)  return 1;
            return 0;
        }
    };

    Pair(int value,int ind)
    {
        this.value = value;
        this.ind = ind;
    }

    public int getInd() 
    {
        return this.ind;
    }
    
    public int getValue() 
    {
        return this.value;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!(obj instanceof Pair))
            return false;
        
        Pair b = (Pair)obj;

        return value==b.value;
    }

    public static Comparator<Pair> getComparator() 
    {
        return comparator;
    }
}



// Collections.sort(numbers);
            
            // Iterator<Integer> itr = numbers.iterator();
            // if(!itr.hasNext()) continue;

            // File file = new File(String.valueOf(fileCount++));
            // splittedFiles.add(file.toString());
            // PrintWriter pw = new PrintWriter(file);

            // pw.print(itr.next());
            // while(itr.hasNext())
            // {
            //     pw.println();
            //     pw.print(itr.next());            
            // }
            // pw.flush();
            // pw.close();
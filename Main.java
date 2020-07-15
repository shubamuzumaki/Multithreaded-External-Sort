class Main
{
    public static void main(String[] args)
    {
        System.out.println("Hello World");
        try 
        {
            long startTime = System.currentTimeMillis();
            RandomGenerator.generateRandomInt("test.txt", 100000000);
            // RandomGenerator.generateRandomInt("test.txt", 29);
            Sort.ExternalSort("test.txt", "sorted.txt",true);
            System.out.println("<<<<<<<<<<<<<: [Time Taken: " + ((System.currentTimeMillis()-startTime)/1000.0) +"s ] :>>>>>>>>>>>>>");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}

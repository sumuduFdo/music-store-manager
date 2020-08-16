public interface StoreManager {

    public void add(MusicItem item);
    public boolean delete(MusicItem item);
    public void printList();
    public void sort();
    public void buy(MusicItem item);
    public void generateReport();


}

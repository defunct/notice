package com.goodworkalan.prattle;

public interface Lister<T>
{
    public Lister<T> add(Object object);
    
    public Lister<T> string(Object object);
    
    public Lister<Lister<T>> list();
    
    public Mapper<Lister<T>> map();

    /**
     * Return the parent element to continue an output statement. This method
     * will only ever return the parent element. It is not necessary to call
     * this method to complete the list.
     * 
     * @return The parent element.
     */
    public T end();
}

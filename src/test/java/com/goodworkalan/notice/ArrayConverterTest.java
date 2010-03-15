package com.goodworkalan.notice;

import static org.testng.Assert.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

import org.testng.annotations.Test;

import com.goodworkalan.diffuse.ArrayConverter;

/**
 * Tests for the array converter.
 *
 * @author Alan Gutierrez
 */
public class ArrayConverterTest {
    /** Test array conversion. */
    @Test
    public void convert() {
        StringBuilder path = new StringBuilder();
        path.append("object");
        Set<String> includes = Collections.emptySet();
        Collection<?> collection = (Collection<?>) ArrayConverter.INSTANCE.convert(new String[] { "a", null }, path, includes);
        Iterator<?> iterator = collection.iterator();
        assertEquals(iterator.next(), "a");
        assertNull(iterator.next());
        assertFalse(iterator.hasNext());
    }
}

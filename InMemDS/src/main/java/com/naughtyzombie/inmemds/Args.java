
package com.naughtyzombie.inmemds;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/** Parse and hold command line arguments as key value pairs.
 */
public class Args {
  /** The key pair dictionary for the arguments store keys and values as String objects.
   */
  private Dictionary d_;
  private String[] m_as;
  
  static Args instance(String[] as) {
	  return new Args(as);
  }
  
  static Args instance(String as) {
	  return instance(argsFromString(as));
  }
  
  public static String[] argsFromString(String args) {

  	if (args == null) {
  		return new String[0];
  	}

              StringTokenizer stok	= new StringTokenizer(args);
              String[] ret	= new String[stok.countTokens()];
              for(int i=0; stok.hasMoreTokens(); i++)
              {
                      ret[i]	= stok.nextToken().trim();
                      ret[i] = ret[i].replaceAll("@s"," ");
              }

              return ret;
  }
  
  /** Common constructor parse the arguments and store in dictionary.
   * @param as the comand line arguments.
   */
  public Args(String[] as) {
    this(as, '-');
  }

  /** Constructor taht defines option prefix for arguments.
   * @param as_in the arguments.
   * @param c the argument prefix.
   */
  public Args(String[] as_in,char c) {
  	
	int len = 0;
	for (int l = 0; l < as_in.length; l++) {
		if (as_in[l] != null) {
			len++;
		}
	}

	m_as = new String[len];

	for (int i = 0,j=0; i < as_in.length; i++) {
		if (as_in[i] != null) {
			m_as[j++] = (String) as_in[i];
		}
	}
  	
    d_ = new Hashtable();
    for(int i = 0; i < m_as.length; i++)
    if(m_as[i] != null && m_as[i].charAt(0) == c)
    {
      String s = m_as[i].substring(1);
      String s1 = null;
      if(i == m_as.length - 1 || m_as[i + 1].charAt(0) == c) {
      	s1 = "yes";
      }
      else
      	s1 = m_as[++i];
      d_.put(s, s1);
    }
  }

  /** Return a iterative sequence of the objects in the dictionary.
   * @return iterative sequence of objects in dictionary.
   */
  public Enumeration elements() {
    return d_.elements();
  }

  /** Return the value object of the key.
   * @param obj key object.
   * @return value object for key.
   */
  public Object get(Object obj) {
    return basicGet(obj);
  }

  /** Tell if their are any arguments in dictionary.
   * @return if the dictionary is empty or not.
   */
  public boolean isEmpty() {
    return d_.isEmpty();
  }

  /** Iterative sequence of argument option names.
   * @return argument option names.
   */
  public Enumeration keys() {
    return d_.keys();
  }

  /** Add the Argument key/value.
   * @param obj the key argument option.
   * @param obj1 the value for this key.
   * @throws NullPointerException if key or value null.
   * @return the value object.
   */
  public Object put(Object obj,Object obj1) throws NullPointerException {
    return d_.put(obj, obj1);
  }

  /** Remove the argument by key.
   * @param obj the argument name key
   * @return the removed value object.
   */
  public Object remove(Object obj) {
    return d_.remove(obj);
  }

  /** Return the number of arguments.
   * @return the number of arguments.
   */
  public int size() {
    return d_.size();
  }

  /** Return boolean value for this argument key.
   * @param s the argument name.
   * @return the boolean value for this name.
   */
  public boolean boolArg(String s) {
    return get(s) != null;
  }

  /** Return the String value for this argument key.
   * @param s the argument name.
   * @param s1 the value default.
   * @return the String value of key or default.
   */
  public String stringArg(String s,String s1) {
    String s2 = (String)get(s);
    if(s2 == null)
    return s1;
    else
    return s2;
  }

  /** Return the int value against argument name.
   * @param s the argument name.
   * @param i the value default.
   * @return the int value of key or default.
   */
  public int intArg(String s,int i) {
    String s1 = stringArg(s, null);
    if(s1 == null)
    return i;
    else
    return Integer.parseInt(s1);
  }

  /** Return the double value against argument name.
   * @param s the argument name.
   * @param d the value default.
   * @return the double value of key or default.
   */
  public double doubleArg(String s,double d) {
    String s1 = stringArg(s, null);
    if(s1 == null)
    return d;
    else
    return Double.valueOf(s1).doubleValue();
  }

  /** Basic internal get value for argument name.
   * @param obj the argument name.
   * @return the value object against argument name.
   */
  protected final Object basicGet(Object obj) {
    return d_.get(obj);
  }
  
  public String toString() {
  	String options = "";
  	for (Enumeration e = d_.keys();e.hasMoreElements();) {
  		String option = (String)e.nextElement();
  		options = options + " -" + option;
  		if (d_.get(option) != null && !"yes".equals(d_.get(option))) {
  			options = options + " " + d_.get(option);
  		}
  	}
  	
  	return options;
  }
  
  public String[] args() {
  	return m_as;
  }
  
  public String args_less(String[] less) {
  	List lessList = Arrays.asList(less);
  	
  	Vector asLess = new Vector();
  	
  	for (Enumeration e = d_.keys();e.hasMoreElements();) {
  		String option = (String)e.nextElement();
  		
  		if (!lessList.contains(option)) {
  			asLess.add("-" + option);
  			if (d_.get(option) != null && !d_.get(option).equals("yes")) {
  				asLess.add(d_.get(option));
  			}
  		}
  	}
  	
  	Object[] l = asLess.toArray();
  	String args = "";
  	for(int i=0;i<l.length;i++) {
  		args = args + l[i] + " ";
  	}
  	
  	return args; 
  }
}

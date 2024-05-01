package user;


//@yahya
public abstract class User 
{
    protected String name;
    protected String id;
    protected int hashedPassword;

    public User()
    {
        
    }
    public User(String name, String id, String password) 
    {
        this.name = name;
        this.id = id;
        this.hashedPassword= password.hashCode();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
    
  
}

package uv.edu.tarea3;

/**
 * Created by Alberto on 09/03/2016.
 */
public class Mensaje {
    private int numero;
    private String usuario;
    private String mensaje;


    public Mensaje(int num, String us, String men)
    {
        numero = num;
        usuario = us;
        mensaje = men;
    }

    public void setUsuario(String user)
    {
        usuario = user;
    }

    public void setMensaje(String mens)
    {
        mensaje = mens;
    }

    public void setNumero(int num)
    {
        numero = num;
    }

    public String getUsuario()
    {
        return usuario;
    }

    public String getMensaje()
    {
        return mensaje;
    }

    public int getNumero()
    {
        return numero;
    }



}

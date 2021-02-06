
import com.ismaplus.intg.cauchy.CauchyInitials;
import com.ismaplus.intg.cauchy.CauchyProblem;
import com.ismaplus.intg.ode.Ode;
import com.ismaplus.intg.ode.OdeSystem;

public class NewModel extends CauchyProblem{
    public NewModel(){
        createInitials();
        createOdeSystem();
}
public void createInitials(){
CauchyInitials cauchyInitials = new CauchyInitials();

        cauchyInitials.setInterval(0,50);
        cauchyInitials.setStepSize(0.5);
        cauchyInitials.setY0(new double[] {});
        cauchyInitials.setError(0.1);

        super.setCauchyInitials(cauchyInitials);
}
public void createOdeSystem(){
OdeSystem odeSystem = new OdeSystem();
        super.setOdeSystem(odeSystem);
    }
    }

package demo.proxy.JDKproxy;

public class Target implements TargetInterface{
    @Override
    public String Hello() {
        System.out.println("hello  目标方法");
        return "目标方法";
    }
}

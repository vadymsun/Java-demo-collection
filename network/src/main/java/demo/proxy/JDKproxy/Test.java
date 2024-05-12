package demo.proxy.JDKproxy;

public class Test {
    public static void main(String[] args) {
        Target target = new Target();
        TargetInterface target_proxy = (TargetInterface) JdkProxyFactory.getProxy(target);
        target_proxy.Hello();

    }
}

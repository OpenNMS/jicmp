import org.opennms.protocols.icmp.*;

public class TestInitialization {

	public static void main(final String[] args) {
		try {
			IcmpSocket socket = new IcmpSocket(1);
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.exit(0);
	}

}

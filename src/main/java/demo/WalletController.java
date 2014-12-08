package demo;

import java.net.URI;
import java.util.UUID;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.justinsb.etcd.EtcdClient;
import com.justinsb.etcd.EtcdClientException;
import com.justinsb.etcd.EtcdResult;



@RestController
public class WalletController {
	String prefix ;
	EtcdClient client;
	String key ;
	WalletController(){
		prefix = "/unittest-" + UUID.randomUUID().toString();
		client = new EtcdClient(URI.create("http://54.183.220.147:4001/"));
		key = "/009982908";
		System.out.println("Connection established successfully");
	}
	
	@RequestMapping(value = "/api/v1/counter", method = RequestMethod.GET)
	public String getCounter() {
		//updateValueToETCD(100);
		String currentCounter = fetchCounterFromEtcd();
		int currentCounterInt = Integer.parseInt(currentCounter);
		System.out.println("The value of current counter :"+currentCounterInt);
		currentCounterInt++;
		updateValueToETCD(currentCounterInt);
		System.out.println("The incremented value of current counter :"+currentCounterInt);
		return String.valueOf(currentCounterInt);
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String getRootCheck() {
		return "System up";
	}
	
	private void updateValueToETCD(int currentCounterInt) {
		//Insert incremented value to etcd.
		try {
			client.set(key, String.valueOf(currentCounterInt));
		} catch (EtcdClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String fetchCounterFromEtcd(){
		//String counter = "";
		EtcdResult result = null;
		try {
			result = client.get(key);
		} catch (EtcdClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Action is :"+result.action);
		System.out.println("The value for the key myKey :"+ result.node.value);
		return result.node.value;
	}
	
}

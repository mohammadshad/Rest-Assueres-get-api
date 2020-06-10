package restgetapi;



import org.testng.Assert;
import org.testng.annotations.Test;



import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;

public class getData {
	
	private Response response;
	getData(){
		this.response  = RestAssured.get("https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=b6907d289e10d714a6e88b30761fae22");
		
		}
	//check response 
	@Test
	public void testResponcecode() {
		
		
		float code = this.response.getStatusCode();
		
		System.out.print("status code is" + code );
		Assert.assertEquals(code, 200);	
	}
	// 1.Is the response contains 4 days of data
	@Test
	public void WeatherMessageBody()
	{
		ResponseBody responsebody = this.response.getBody();
		List[] forecast = this.response.jsonPath().getObject("list",List[].class );
	   String responsestring = responsebody.asString();
	   List first = forecast[0];
	   List last = forecast[forecast.length-1];
	   int interval = last.dt- first.dt;
	   
		if(interval>=342000) {
			
			System.out.print("response contains 4 days of data");
		}
		else {
			System.out.print("response not contains 4 days of data");
			}
		
	}
	
	//2. Is all the forecast in the hourly interval ( no hour should be missed )
	@Test
	public void is_forecast_hourly() {
		
		List[] interval = this.response.jsonPath().getObject("list",List[].class );
		Integer previousDt = 0;boolean i=false;
//		interval[1].setDt(interval[1].getDt()+100);
		for(List obj : interval)
		{
		   Integer dt = obj.getDt();
		   if(!i) {
			   i=true;
			}else{
//				System.out.println(dt-previousDt);
				Assert.assertFalse((dt-previousDt) >3600);
			}
		   previousDt = dt;
		} 	  
		  
	}
	
	//4. If the weather id is 500, the description should be light rain
	@Test
	public void weather_message_description() {
		
		List[] interval = this.response.jsonPath().getObject("list",List[].class );
		   
		for(List obj : interval)
		{
		   for(Weather weather: obj.getWeather()) {
			  // System.out.println(weather.getMain());
			   switch(weather.getId()) {
			   case 500:
				   Assert.assertEquals(weather.getDescription().toLowerCase(), "light rain");
				   
			   }
		   }
		} 	  
		  
	}
	
	//5. If the weather id is 800, the description should be a clear sky 
	@Test
public void weather_message_description_light_rain() {
		
		List[] interval = this.response.jsonPath().getObject("list",List[].class );
		   
		for(List obj : interval)
		{
		   for(Weather weather: obj.getWeather()) {
			   switch(weather.getId()) {
			   case 800:
				   Assert.assertEquals(weather.getDescription().toLowerCase(), "clear sky");
				   
			   }
		   }
		} 	  
		  
	} 
	
	
	//For all 4 days, the temp should not be less than temp_min and not more than temp_max
	//Note there is a mapping  issue to find temp_min and temp_max.
	
	
	@Test
public void temp_description() {
		
		
		List[] interval = this.response.jsonPath().getObject("list",List[].class );
		
		ResponseBody responsebody = this.response.getBody();
		//List[] hours = this.response.jsonPath().getObject("list",List[].class );
	   System.out.println(responsebody.asString());
		for(List obj : interval)
		{
			//System.out.println(obj.getDtTxt());
			Main main=obj.getMain();
			
				/*
				if(main.getTemp()<=main.getTempMin()) {
					System.out.println(main.getTemp());	
					
	
					
				}
			if(main.getTemp()>=main.getTempMax()) {
				System.out.println(main.getTemp());
			}*/
			//			for(Main main:obj.getMain())
//			System.out.println(main.getTemp());
		}
		 		} 	  
		 
	}


	
	
	


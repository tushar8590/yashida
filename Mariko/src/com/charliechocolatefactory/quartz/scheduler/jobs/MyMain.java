package com.charliechocolatefactory.quartz.scheduler.jobs;
import java.util.List;

import com.google.common.base.Splitter;


public class MyMain {

	public static void main(String[] args) {
		
		String str = "\"39779067\",\"A137SHM514403\",\"Black And Green Canvas Sports Shoes\",\"<p> This pair of sports shoes is specifically designed for football lovers. </p><ul><li> Round toed, black sports shoes with green accents. </li><li> Low top styling </li><li> Central lace-ups. </li><li> Synthetic upper with stitched and overlay detail. </li><li> Contrast design details on the quarter. </li><li> Soft and cushioned foot-bed with branding at the heel. </li><li> Tough and textured rubber out-sole with patterned grooves. </li><li> Foxing at the fore-foot.  </li></ul><p>  Super comfy and versatile, team it with your track pants for a refined style. </p><b>Material: Canvas and Synthetic (Upper) and Airmax (Sole)</b><br />Weight : 298gm (per single shoe) - Weight of the product may vary depending on size\",\"499.00\",\"INR\",\"0.00\",\"499.00\",\"http://clk.omgt5.com/?AID=769090&PID=8422&Type=12&r=http://zovi.com/black-and-green-canvas-sports-shoes--A137SHM514403\",\"8422\",\"218153\",\"\",\"http://d3syg3ktxig1hl.cloudfront.net/bd2/g/p/A137SHM514403/1_z.jpg\",\"\",\"\",\"Active\",\"\",\"\",\"\",\"New - No\",\"\",\"\",\"\",\"\",\"Mens Footwear\",\"Root|Mens Footwear|Mens Footwear|\"";
		
		
		Splitter splitter = Splitter.on(",");
		/*Iterable<String> numbers = splitter.split(str);
		for(String number : numbers) {
			System.out.println(number);
			}*/
		List<String> lst = splitter.splitToList(str);
		Object arr[] = lst.toArray();
		for(Object ob:arr){
			System.out.println(ob);
		}
	}

}

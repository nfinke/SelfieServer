package selfieserver;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class SelfieController {
	
	/*
	 * FILTERS - id and humanreadable name of the image filters supported by the system
	 * 
	 * Statically defined at buildtime for the time beeing. Could be made more dynamic in the future
	 * by having the definition for the filters in a configuration file for example
	 * 
	 */
	static final Hashtable<Integer,String> FILTERS = new Hashtable<Integer,String> ();
	static
	{
		FILTERS.put(1, "Black & White");
		FILTERS.put(2, "Charcoal");
		FILTERS.put(3, "Outline");
	};
	
	/*
	 * getFilters
	 * 
	 * returns a list of all filters that can be applied to a selfie.
	 * 
	 * id - identification of the filter
	 * name - human readable name of the filter (for display in the mobile app for example)
	 * 
	 */
	@RequestMapping(value="/filters", method=RequestMethod.GET)
	public Hashtable<Integer,String> getFilters() {
		return FILTERS;
	}
	
	/*
	 * processSelfie for uploading new selfie
	 * 
	 * selfie as multipart file
	 * 
	 * list of filters to be applied in RequestBody 
	 * 
	 * returns link to newly created ressource
	 * 
	 */
	@RequestMapping (value="/selfies", method=RequestMethod.POST, headers = "content-type=multipart/*")
	public @ResponseBody String processSelfie(@RequestParam String name, @RequestParam MultipartFile file, @RequestBody Integer[] filters) {
		if(!file.isEmpty()){
			// Generate identifier
			long now = System.currentTimeMillis();			
			try {
				byte[] bytes = file.getBytes();
				// create temporary file
				File tmpFile = File.createTempFile("selfie_"+String.valueOf(now), "tmp");
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(tmpFile));
				stream.write(bytes);
				stream.close();
				// check for each filter requested by the user if it is supported by the implementation
				// ToDo: make sure that each filter is only applied once
				for(Integer filter : filters){
					// if filter is not supported return status code 501 (NOT IMPLEMENTED)
					if(FILTERS.containsKey(filter)){
						System.out.println("Applying filter "+filter+"");
					}
				}
				System.out.println("to selfie");						
				return "Selfie has been uploaded successfully";
			}catch(Exception ex){
				return "Selfie upload failed: " + ex.getMessage();
			}
		} else {
			return "Selfie upload failed: File was empty";
		}
	}
	
	/*
	 * Deleting Selfie from Server
	 * 
	 * Selfie to be deleted is identified by it's ID
	 * 
	 */
	@RequestMapping (value="/selfie/{selfie}", method=RequestMethod.DELETE)
	public @ResponseBody String deleteSelfie(@PathVariable Integer selfie){
		return "Selfie "+selfie+" has been removed from server";
	}
	
}

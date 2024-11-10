package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.TelefonoInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/phone")
public class TelefonoControllerV1 {
    
    @Autowired
    private TelefonoInputAdapterRest phoneInputAdapterRest;
    
    @ResponseBody
    @GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TelefonoResponse> personas(@PathVariable String database) {
        log.info("Into personas REST API");
            return phoneInputAdapterRest.historial(database.toUpperCase());
    }
    
    @ResponseBody
    @PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TelefonoResponse crearPersona(@RequestBody TelefonoRequest request) throws NumberFormatException, NoExistException {
		log.info("esta en el metodo crearTarea en el controller del api");
		return phoneInputAdapterRest.crearPhone(request);
	}

	@ResponseBody
	@PutMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse actualizarPersona(@RequestBody TelefonoRequest request) {
		log.info("esta en el metodo actualizarTarea en el controller del api");
		return phoneInputAdapterRest.actualizarPhone(request);
	}

	@ResponseBody
	@DeleteMapping(path = "/{number}/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse eliminarPersona(@PathVariable String number, @PathVariable String database) throws InvalidOptionException {
		log.info("esta en el metodo eliminarTarea en el controller del api");
		return phoneInputAdapterRest.eliminarPhone(database, number);
	}

	@ResponseBody
	@GetMapping(path = "/{number}/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TelefonoResponse persona(@PathVariable String number, @PathVariable String database) {
		log.info("Into persona REST API");
		return phoneInputAdapterRest.buscarPhone(database, number);
	}
}
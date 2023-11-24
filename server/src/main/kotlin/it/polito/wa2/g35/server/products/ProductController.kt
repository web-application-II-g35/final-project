package it.polito.wa2.g35.server.products

import io.micrometer.observation.annotation.Observed
import it.polito.wa2.g35.server.exceptions.BadRequestException
import it.polito.wa2.g35.server.profiles.ProfileNotFoundException
import it.polito.wa2.g35.server.profiles.customer.CustomerDTO
import it.polito.wa2.g35.server.ticketing.ticket.TicketController
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = ["http://localhost:5000"])
class ProductController(private val productService: ProductService) {
    private val log: Logger = LoggerFactory.getLogger(TicketController::class.java)
    @GetMapping("/products/")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('Client', 'Manager', 'Expert')")
    @Observed(
        name = "/products/",
        contextualName = "get-products-request"
    )
    fun getAll(): List<ProductDTO> {
        log.info("Get products request successful")
        return productService.getAll()
    }

    @GetMapping("/products/{productId}")
    @PreAuthorize("hasAnyRole('Client', 'Manager', 'Expert')")
    @ResponseStatus(HttpStatus.OK)
    @Observed(
        name = "/products/{productId}",
        contextualName = "get-product-by-id-request"
    )
    fun getProduct(@PathVariable productId: String): ProductDTO? {
        log.info("Get product by Id request successful")
        return productService.getProductById(productId)
    }

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('Manager')")
    @Observed(
        name = "/products",
        contextualName = "post-product-request"
    )
    fun postProduct(
        @RequestBody @Valid p: ProductDTO,
        br: BindingResult
    ) : ProductDTO? {
        if (br.hasErrors()) {
            log.error("Create Product request failed by bad request format")
            throw BadRequestException("Bad request format!")
        }
        else {
            log.info("Create Product request successful")
            return productService.createProduct(p)
        }
    }

    @PutMapping("/products/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Manager')")
    @Observed(
        name = "/customers/{email}",
        contextualName = "put-profile-request"
    )
    fun updateProfile(
        @PathVariable("id") id: String,
        @RequestBody @Valid p: ProductDTO,
        br: BindingResult
    ) : ProductDTO? {
        if (br.hasErrors()){
            log.error("Update Profile failed by bad request format")
            throw BadRequestException("Bad request format!")
        }
        else
            if (id == p.id) {
                log.info("Update Profile request successful")
                return productService.updateProduct(p)
            } else {
                log.error("Profile with given email doesn't exists!")
                throw ProfileNotFoundException("Profile with given email doesn't exists!")
            }
    }
}
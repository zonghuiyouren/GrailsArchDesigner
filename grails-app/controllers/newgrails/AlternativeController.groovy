package newgrails

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AlternativeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Alternative.list(params), model:[alternativeCount: Alternative.count()]
    }

    def show(Alternative alternative) {
        respond alternative
    }

    def create() {
        respond new Alternative(params)
    }

    @Transactional
    def save(Alternative alternative) {
        if (alternative == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (alternative.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond alternative.errors, view:'create'
            return
        }

        alternative.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'alternative.label', default: 'Alternative'), alternative.id])
                redirect alternative
            }
            '*' { respond alternative, [status: CREATED] }
        }
    }

    def edit(Alternative alternative) {
        respond alternative
    }

    @Transactional
    def update(Alternative alternative) {
        if (alternative == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (alternative.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond alternative.errors, view:'edit'
            return
        }

        alternative.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'alternative.label', default: 'Alternative'), alternative.id])
                redirect alternative
            }
            '*'{ respond alternative, [status: OK] }
        }
    }

    @Transactional
    def delete(Alternative alternative) {

        if (alternative == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        alternative.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'alternative.label', default: 'Alternative'), alternative.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'alternative.label', default: 'Alternative'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

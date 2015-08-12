package newgrails

import grails.test.mixin.*
import spock.lang.*

@TestFor(DecisionController)
@Mock(Decision)
class DecisionControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.decisionList
            model.decisionCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.decision!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def decision = new Decision()
            decision.validate()
            controller.save(decision)

        then:"The create view is rendered again with the correct model"
            model.decision!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            decision = new Decision(params)

            controller.save(decision)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/decision/show/1'
            controller.flash.message != null
            Decision.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def decision = new Decision(params)
            controller.show(decision)

        then:"A model is populated containing the domain instance"
            model.decision == decision
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def decision = new Decision(params)
            controller.edit(decision)

        then:"A model is populated containing the domain instance"
            model.decision == decision
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/decision/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def decision = new Decision()
            decision.validate()
            controller.update(decision)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.decision == decision

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            decision = new Decision(params).save(flush: true)
            controller.update(decision)

        then:"A redirect is issued to the show action"
            decision != null
            response.redirectedUrl == "/decision/show/$decision.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/decision/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def decision = new Decision(params).save(flush: true)

        then:"It exists"
            Decision.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(decision)

        then:"The instance is deleted"
            Decision.count() == 0
            response.redirectedUrl == '/decision/index'
            flash.message != null
    }
}

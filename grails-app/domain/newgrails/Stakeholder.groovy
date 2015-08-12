package newgrails

class Stakeholder {

    String stakeholerName;
    String remark;

    static mapping = {
        remark type: 'text'
    }

    static belongsTo = [app : ArchApplication]


    static hasMany = [decision: Decision]

    static constraints = {
    }
}

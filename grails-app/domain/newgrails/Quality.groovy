package newgrails

class Quality {

    String qualityName;

    static belongsTo = [app: ArchApplication]

    static hasMany = [decision: Decision]

    static constraints = {
    }
}

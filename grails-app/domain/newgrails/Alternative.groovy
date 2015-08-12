package newgrails

class Alternative {
    String alternativeName;

    static belongsTo = [app: ArchApplication, decision: Decision]

    static constraints = {
    }
}

/** game entities that can cause damage to other entities should implement this interface
 */
public interface DamageInflictable {
    /** method to print details of how the calling game entity damaged the input character
     * where character receives the damage.
     */
    void printDamageLog(Character character);
}

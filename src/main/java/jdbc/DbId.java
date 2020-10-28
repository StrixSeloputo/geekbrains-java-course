package jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbId {
    // Поле, помеченное аннотацией @DbId, должно быть отображено в столбец с именем id, такое поле является обязательным
}

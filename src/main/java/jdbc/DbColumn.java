package jdbc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DbColumn {
    // Все поля, отмеченные аннотацией @DbColumn должны быть сохранены в таблицу (имя поля совпадает с именем столбца в таблице)
}

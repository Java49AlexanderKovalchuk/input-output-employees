package telran.employees.dto;

import java.io.Serializable;

public record UpdateData<T extends Serializable>(long id, T data) implements Serializable {

}

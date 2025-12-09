package auditaspect.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Сервис для логирования действий пользователей.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditLogger {

    private final ObjectMapper objectMapper;

    /**
     * Логирование объекта при создании.
     *
     * @param arg объект для логирования
     */
    public void logCreate(Object arg) {
        try {
            String json = objectMapper.writeValueAsString(arg);
            log.trace("Аудит: объект = {}", json);
        } catch (JsonProcessingException e) {
            log.warn("Не удалось сериализовать объект для аудита", e);
        }
    }

    /**
     * Логирование GTIN удаляемого объекта.
     *
     * @param arg объект с методом getGtin()
     */
    public void logDelete(Object arg) {
        try {
            Object gtinObj = arg.getClass().getMethod("getGtin").invoke(arg);
            if (gtinObj != null) {
                log.trace("Аудит: удаляемый объект gtin = {}", gtinObj);
            }
        } catch (Exception e) {
            log.warn("Не удалось получить gtin из объекта для аудита", e);
        }
    }
}
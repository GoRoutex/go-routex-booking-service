package vn.com.routex.hub.booking.service.infrastructure.persistence.adapter.vehicle;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import vn.com.routex.hub.booking.service.domain.common.PagedResult;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleTemplateCategory;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleTemplateStatus;
import vn.com.routex.hub.booking.service.domain.vehicle.VehicleTemplateType;
import vn.com.routex.hub.booking.service.domain.vehicle.model.VehicleTemplate;
import vn.com.routex.hub.booking.service.domain.vehicle.port.VehicleTemplateRepositoryPort;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.entity.VehicleTemplateEntity;
import vn.com.routex.hub.booking.service.infrastructure.persistence.jpa.vehicle.repository.VehicleTemplateRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class VehicleTemplateRepositoryAdapter implements VehicleTemplateRepositoryPort {

    private final VehicleTemplateRepository vehicleTemplateRepository;
    private final VehicleTemplatePersistenceMapper vehicleTemplatePersistenceMapper;

    @Override
    public Optional<VehicleTemplate> findByCategoryAndType(String category, String type) {
        return vehicleTemplateRepository.findFirstByCategoryAndTypeAndStatus(
                        VehicleTemplateCategory.valueOf(category.trim()),
                        VehicleTemplateType.valueOf(type.trim()),
                        VehicleTemplateStatus.ACTIVE)
                .map(vehicleTemplatePersistenceMapper::toDomain);
    }

    @Override
    public Optional<VehicleTemplate> findByCategoryAndType(String category, String type, String merchantId) {
        return vehicleTemplateRepository.findFirstByCategoryAndTypeAndMerchantIdAndStatus(
                        VehicleTemplateCategory.valueOf(category.trim()),
                        VehicleTemplateType.valueOf(type.trim()),
                        merchantId,
                        VehicleTemplateStatus.ACTIVE)
                .map(vehicleTemplatePersistenceMapper::toDomain);
    }

    @Override
    public Optional<VehicleTemplate> findById(String id) {
        return vehicleTemplateRepository.findById(id)
                .map(vehicleTemplatePersistenceMapper::toDomain)
                .filter(template -> template.getStatus() == VehicleTemplateStatus.ACTIVE);
    }

    @Override
    public Optional<VehicleTemplate> findById(String id, String merchantId) {
        return vehicleTemplateRepository.findByIdAndMerchantId(id, merchantId)
                .map(vehicleTemplatePersistenceMapper::toDomain)
                .filter(template -> template.getStatus() == VehicleTemplateStatus.ACTIVE);
    }

    @Override
    public Map<String, VehicleTemplate> findByIds(List<String> ids) {
        return vehicleTemplateRepository.findAllById(ids).stream()
                .map(vehicleTemplatePersistenceMapper::toDomain)
                .filter(template -> template.getStatus() == VehicleTemplateStatus.ACTIVE)
                .collect(Collectors.toMap(VehicleTemplate::getId, template -> template));
    }

    @Override
    public boolean existsByCode(String code, String merchantId) {
        return vehicleTemplateRepository.existsByCodeAndMerchantId(code, merchantId);
    }

    @Override
    public boolean existsByCategoryAndType(String category, String type, String merchantId) {
        return vehicleTemplateRepository.existsByCategoryAndTypeAndMerchantId(
                VehicleTemplateCategory.valueOf(category.trim()),
                VehicleTemplateType.valueOf(type.trim()),
                merchantId);
    }

    @Override
    public void save(VehicleTemplate template) {
        vehicleTemplateRepository.save(vehicleTemplatePersistenceMapper.toEntity(template));
    }

    @Override
    public PagedResult<VehicleTemplate> fetch(
            String merchantId,
            VehicleTemplateStatus status,
            VehicleTemplateCategory category,
            VehicleTemplateType type,
            int pageNumber,
            int pageSize
    ) {
        Page<VehicleTemplateEntity> page = vehicleTemplateRepository.findByFilters(
                merchantId,
                status,
                category,
                type,
                PageRequest.of(pageNumber, pageSize));
        List<VehicleTemplate> items = page.getContent().stream()
                .map(vehicleTemplatePersistenceMapper::toDomain)
                .toList();

        return PagedResult.<VehicleTemplate>builder()
                .items(items)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}

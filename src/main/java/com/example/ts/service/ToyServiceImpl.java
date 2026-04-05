package com.example.ts.service;

import com.example.ts.cache.ToySearchKey;
import com.example.ts.domain.Brand;
import com.example.ts.domain.Category;
import com.example.ts.domain.Toy;
import com.example.ts.dto.ToyRequestDto;
import com.example.ts.dto.ToyResponseDto;
import com.example.ts.exception.AppException;
import com.example.ts.mapper.ToyMapper;
import com.example.ts.repository.BrandRepository;
import com.example.ts.repository.CategoryRepository;
import com.example.ts.repository.OrderItemRepository;
import com.example.ts.repository.ToyRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ToyServiceImpl implements ToyService {

  private static final Logger log = LoggerFactory.getLogger(ToyServiceImpl.class);
  private static final String BRAND_NOT_FOUND = "Brand not found";
  private static final String CATEGORY_NOT_FOUND = "Category not found";
  private final ToyRepository repository;
  private final ToyMapper mapper;
  private final BrandRepository brandRepository;
  private final CategoryRepository categoryRepository;
  private final OrderItemRepository orderItemRepository;

  private final Map<ToySearchKey, Page<ToyResponseDto>> cache = new HashMap<>();

  public ToyServiceImpl(ToyRepository repository,
                        ToyMapper mapper,
                        BrandRepository brandRepository,
                        CategoryRepository categoryRepository,
                        OrderItemRepository orderItemRepository) {
    this.repository = repository;
    this.mapper = mapper;
    this.brandRepository = brandRepository;
    this.categoryRepository = categoryRepository;
    this.orderItemRepository = orderItemRepository;
  }

  @Override
  public List<ToyResponseDto> getAllToys() {
    return repository.findAllWithCategories()
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Override
  public ToyResponseDto getToyById(Long id) {
    Toy toy = repository.findByIdWithCategories(id)
        .orElseThrow(this::toyNotFound);
    return mapper.toDto(toy);
  }

  @Override
  public List<ToyResponseDto> getToysByName(String name) {
    return repository.findByName(name)
        .stream()
        .map(mapper::toDto)
        .toList();
  }

  @Override
  public ToyResponseDto createToy(ToyRequestDto dto) {

    Brand brand = brandRepository.findById(dto.getBrandId())
        .orElseThrow(() -> new AppException(BRAND_NOT_FOUND, 404));

    Set<Category> categories = dto.getCategoryIds()
        .stream()
        .map(id -> categoryRepository.findById(id)
            .orElseThrow(() -> new AppException(CATEGORY_NOT_FOUND, 404)))
        .collect(Collectors.toSet());

    Toy toy = new Toy();
    toy.setName(dto.getName());
    toy.setPrice(dto.getPrice());
    toy.setQuantity(dto.getQuantity());
    toy.setBrand(brand);
    toy.setCategories(categories);

    Toy saved = repository.save(toy);

    cache.clear();

    return mapper.toDto(saved);
  }

  @Override
  public ToyResponseDto updateToy(Long id, ToyRequestDto dto) {

    Toy toy = repository.findById(id)
        .orElseThrow(this::toyNotFound);

    Brand brand = brandRepository.findById(dto.getBrandId())
        .orElseThrow(() -> new AppException(BRAND_NOT_FOUND, 404));
    Set<Category> categories = dto.getCategoryIds()
        .stream()
        .map(catId -> categoryRepository.findById(catId)
            .orElseThrow(() -> new AppException(CATEGORY_NOT_FOUND, 404)))
        .collect(Collectors.toSet());

    toy.setName(dto.getName());
    toy.setPrice(dto.getPrice());
    toy.setQuantity(dto.getQuantity());
    toy.setBrand(brand);
    toy.setCategories(categories);

    Toy updated = repository.save(toy);
    cache.clear();

    return mapper.toDto(updated);
  }

  @Override
  @Transactional
  public void deleteToy(Long id) {

    Toy toy = repository.findById(id)
        .orElseThrow(this::toyNotFound);

    if (orderItemRepository.existsByToy_Id(id)) {
      throw new AppException("Toy is used in orders and cannot be deleted", 400);
    }

    repository.delete(toy);
    cache.clear();
  }

  private AppException toyNotFound() {
    return new AppException("Toy not found", 404);
  }

  @Override
  public Page<ToyResponseDto> getByCategoryAndPrice(
      String category,
      Double minPrice,
      int page,
      int size
  ) {

    ToySearchKey key = new ToySearchKey(category, minPrice, page, size);

    if (cache.containsKey(key)) {
      log.info("FROM CACHE");
      return cache.get(key);
    }

    Page<Toy> toys = repository.findByCategoryAndPrice(
        category,
        minPrice,
        PageRequest.of(page, size)
    );

    Page<ToyResponseDto> result = toys.map(mapper::toDto);
    cache.put(key, result);

    return result;
  }

  @Override
  public Page<ToyResponseDto> getByCategoryAndPriceNative(
      String category,
      Double minPrice,
      int page,
      int size
  ) {

    Page<Toy> toys = repository.findByCategoryAndPriceNative(
        category,
        minPrice,
        PageRequest.of(page, size)
    );

    return toys.map(mapper::toDto);
  }

  @Override
  public List<ToyResponseDto> createToysBulk(List<ToyRequestDto> dtos) {

    return dtos.stream()
        .map(dto -> {

          Brand brand = brandRepository.findById(dto.getBrandId())
              .orElseThrow(() -> new AppException(BRAND_NOT_FOUND, 404));

          Set<Category> categories = dto.getCategoryIds()
              .stream()
              .map(id -> categoryRepository.findById(id)
                  .orElseThrow(() -> new AppException(CATEGORY_NOT_FOUND, 404)))
              .collect(Collectors.toSet());

          Toy toy = new Toy();
          toy.setName(dto.getName());
          toy.setPrice(dto.getPrice());
          toy.setQuantity(dto.getQuantity());
          toy.setBrand(brand);
          toy.setCategories(categories);

          return repository.save(toy);

        })
        .map(mapper::toDto)
        .toList();
  }

  @Override
  public List<ToyResponseDto> createToysBulkNoTx(List<ToyRequestDto> dtos) {

    return dtos.stream()
        .map(dto -> {

          if ("ERROR".equals(dto.getName())) {
            throw new AppException("Test error", 400);
          }

          Brand brand = brandRepository.findById(dto.getBrandId())
              .orElseThrow(() -> new AppException(BRAND_NOT_FOUND, 404));

          Set<Category> categories = dto.getCategoryIds().stream()
              .map(id -> categoryRepository.findById(id)
                  .orElseThrow(() -> new AppException(CATEGORY_NOT_FOUND, 404)))
              .collect(Collectors.toSet());

          Toy toy = new Toy();
          toy.setName(dto.getName());
          toy.setPrice(dto.getPrice());
          toy.setQuantity(dto.getQuantity());
          toy.setBrand(brand);
          toy.setCategories(categories);

          return repository.save(toy);

        })
        .map(mapper::toDto)
        .toList();
  }

  @Override
  @Transactional
  public List<ToyResponseDto> createToysBulkTx(List<ToyRequestDto> dtos) {

    return dtos.stream()
        .map(dto -> {

          if ("ERROR".equals(dto.getName())) {
            throw new AppException("Test error", 400);
          }

          Brand brand = brandRepository.findById(dto.getBrandId())
              .orElseThrow(() -> new AppException(BRAND_NOT_FOUND, 404));

          Set<Category> categories = dto.getCategoryIds().stream()
              .map(id -> categoryRepository.findById(id)
                  .orElseThrow(() -> new AppException(CATEGORY_NOT_FOUND, 404)))
              .collect(Collectors.toSet());

          Toy toy = new Toy();
          toy.setName(dto.getName());
          toy.setPrice(dto.getPrice());
          toy.setQuantity(dto.getQuantity());
          toy.setBrand(brand);
          toy.setCategories(categories);

          return repository.save(toy);

        })
        .map(mapper::toDto)
        .toList();
    }
  }
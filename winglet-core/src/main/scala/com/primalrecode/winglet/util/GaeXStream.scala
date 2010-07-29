package com.primalrecode.winglet.util

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider
import com.thoughtworks.xstream.converters.basic._
import com.thoughtworks.xstream.converters._
import com.thoughtworks.xstream.converters.reflection._
import com.thoughtworks.xstream.converters.collections._
import extended._
import XStream._
import com.thoughtworks.xstream.io.HierarchicalStreamDriver
import com.thoughtworks.xstream.io.xml.XppDriver
import com.thoughtworks.xstream.core.util.{CompositeClassLoader, ClassLoaderReference}
import com.thoughtworks.xstream.mapper._
import com.thoughtworks.xstream.core.{DefaultConverterLookup, JVM}


class GaeXStream(reflectionProvider: ReflectionProvider, driver: HierarchicalStreamDriver, classLoaderReference: ClassLoaderReference, mapper: Mapper, converterLookup:DefaultConverterLookup)
        extends XStream(reflectionProvider, driver, classLoaderReference, mapper, converterLookup, converterLookup) {

  override def setupConverters = {
    val reflectionConverter: ReflectionConverter = new ReflectionConverter(mapper, reflectionProvider)
    registerConverter(reflectionConverter, PRIORITY_VERY_LOW)
    registerConverter(new SerializableConverter(mapper, reflectionProvider), PRIORITY_LOW)
    registerConverter(new ExternalizableConverter(mapper), PRIORITY_LOW)
    registerConverter(new NullConverter, PRIORITY_VERY_HIGH)
    registerConverter(new IntConverter, PRIORITY_NORMAL)
    registerConverter(new FloatConverter, PRIORITY_NORMAL)
    registerConverter(new DoubleConverter, PRIORITY_NORMAL)
    registerConverter(new LongConverter, PRIORITY_NORMAL)
    registerConverter(new ShortConverter, PRIORITY_NORMAL)
    registerConverter((new CharConverter).asInstanceOf[Converter], PRIORITY_NORMAL)
    registerConverter(new BooleanConverter, PRIORITY_NORMAL)
    registerConverter(new ByteConverter, PRIORITY_NORMAL)
    registerConverter(new StringConverter, PRIORITY_NORMAL)
    registerConverter(new StringBufferConverter, PRIORITY_NORMAL)
    registerConverter(new DateConverter, PRIORITY_NORMAL)
    registerConverter(new BitSetConverter, PRIORITY_NORMAL)
    registerConverter(new URLConverter, PRIORITY_NORMAL)
    registerConverter(new BigIntegerConverter, PRIORITY_NORMAL)
    registerConverter(new BigDecimalConverter, PRIORITY_NORMAL)
    registerConverter(new ArrayConverter(mapper), PRIORITY_NORMAL)
    registerConverter(new CharArrayConverter, PRIORITY_NORMAL)
    registerConverter(new CollectionConverter(mapper), PRIORITY_NORMAL)
    registerConverter(new MapConverter(mapper), PRIORITY_NORMAL)
    registerConverter(new TreeMapConverter(mapper), PRIORITY_NORMAL)
    registerConverter(new TreeSetConverter(mapper), PRIORITY_NORMAL)
    registerConverter(new PropertiesConverter, PRIORITY_NORMAL)
    registerConverter(new EncodedByteArrayConverter, PRIORITY_NORMAL)
    registerConverter(new FileConverter, PRIORITY_NORMAL)
    registerConverter(new DynamicProxyConverter(mapper, classLoaderReference), PRIORITY_NORMAL)
    registerConverter(new JavaClassConverter(classLoaderReference), PRIORITY_NORMAL)
    registerConverter(new JavaMethodConverter(classLoaderReference), PRIORITY_NORMAL)
    registerConverter(new LocaleConverter, PRIORITY_NORMAL)
    registerConverter(new GregorianCalendarConverter, PRIORITY_NORMAL)

//    if (JVM.is14) {
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.extended.SubjectConverter", PRIORITY_NORMAL, Array[Class[_]](classOf[Mapper]), Array[Any](mapper))
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.extended.ThrowableConverter", PRIORITY_NORMAL, Array[Class[_]](classOf[Converter]), Array[Any](reflectionConverter))
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.extended.StackTraceElementConverter", PRIORITY_NORMAL, null, null)
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.extended.CurrencyConverter", PRIORITY_NORMAL, null, null)
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.extended.RegexPatternConverter", PRIORITY_NORMAL, Array[Class[_]](classOf[Converter]), Array[Any](reflectionConverter))
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.extended.CharsetConverter", PRIORITY_NORMAL, null, null)
//    }
//
//    if (JVM.is15) {
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.extended.DurationConverter", PRIORITY_NORMAL, null, null)
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.enums.EnumConverter", PRIORITY_NORMAL, null, null)
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.enums.EnumSetConverter", PRIORITY_NORMAL, Array[Class[_]](classOf[Mapper]), Array[Any](mapper))
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.enums.EnumMapConverter", PRIORITY_NORMAL, Array[Class[_]](classOf[Mapper]), Array[Any](mapper))
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.basic.StringBuilderConverter", PRIORITY_NORMAL, null, null)
//      dynamicallyRegisterConverter("com.thoughtworks.xstream.converters.basic.UUIDConverter", PRIORITY_NORMAL, null, null)
//    }

    registerConverter(new SelfStreamingInstanceChecker(reflectionConverter, this), PRIORITY_NORMAL)
  }
}

object GaeXStream {
  val classLoaderReference = new ClassLoaderReference(new CompositeClassLoader)
  var converterLookup = new DefaultConverterLookup

  def create = new GaeXStream(new PureJavaReflectionProvider, new XppDriver, classLoaderReference, buildMapper, converterLookup)


  private def buildMapper: Mapper = {
    var mapper: Mapper = new DefaultMapper(classLoaderReference)
    mapper = new DynamicProxyMapper(mapper)
    mapper = new PackageAliasingMapper(mapper)
    mapper = new ClassAliasingMapper(mapper)
    mapper = new FieldAliasingMapper(mapper)
    mapper = new AttributeAliasingMapper(mapper)
    mapper = new SystemAttributeAliasingMapper(mapper)
    mapper = new ImplicitCollectionMapper(mapper)
    mapper = new OuterClassMapper(mapper)
    mapper = new ArrayMapper(mapper)
    mapper = new DefaultImplementationsMapper(mapper)
    mapper = new AttributeMapper(mapper, converterLookup)
    mapper = new LocalConversionMapper(mapper)
    mapper = new ImmutableTypesMapper(mapper)
    mapper = new CachingMapper(mapper)
    return mapper
  }
}
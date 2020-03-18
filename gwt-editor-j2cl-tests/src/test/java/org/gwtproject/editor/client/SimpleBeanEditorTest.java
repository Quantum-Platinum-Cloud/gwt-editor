/*
 * Copyright © 2018 The GWT Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.editor.client;

import static org.junit.Assert.*;

import com.google.j2cl.junit.apt.J2clTestInput;
import java.util.*;
import org.gwtproject.editor.client.adapters.EditorSource;
import org.gwtproject.editor.client.adapters.ListEditor;
import org.gwtproject.editor.client.adapters.OptionalFieldEditor;
import org.gwtproject.editor.client.adapters.SimpleEditor;
import org.gwtproject.editor.client.annotation.IsDriver;
import org.junit.Test;

/**
 * Uses the SimpleBeanEditorTest to test core Editor behaviors as generated by
 * AbstractEditorDriverGenerator.
 */
@J2clTestInput(SimpleBeanEditorTest.class)
public class SimpleBeanEditorTest {

  class AddressCoEditorView extends LeafAddressEditor implements IsEditor<LeafAddressEditor> {
    private LeafAddressEditor addressEditor = new LeafAddressEditor();

    @Override
    public LeafAddressEditor asEditor() {
      return addressEditor;
    }
  }

  class AddressEditorPartOne implements Editor<Address> {
    SimpleEditor<String> city = SimpleEditor.of(UNINITIALIZED);
  }

  class AddressEditorPartTwo implements Editor<Address> {
    SimpleEditor<String> street = SimpleEditor.of(UNINITIALIZED);
  }

  class AddressEditorView implements IsEditor<LeafAddressEditor> {
    LeafAddressEditor addressEditor = new LeafAddressEditor();

    @Override
    public LeafAddressEditor asEditor() {
      return addressEditor;
    }
  }

  static class DepartmentWithList extends Department {
    List<Intern> interns = new ArrayList<Intern>();

    public List<Intern> getInterns() {
      return interns;
    }

    public void setInterns(List<Intern> interns) {
      this.interns = new ArrayList<Intern>(interns);
    }
  }

  static class DepartmentWithListEditor implements Editor<DepartmentWithList> {
    PersonEditor manager = new PersonEditor();
    ListEditor<Intern, PersonEditor> interns =
        ListEditor.of(
            new EditorSource<PersonEditor>() {
              @Override
              public PersonEditor create(int index) {
                return new PersonEditor();
              }
            });
  }

  @IsDriver
  interface DepartmentWithListEditorDriver
      extends SimpleBeanEditorDriver<DepartmentWithList, DepartmentWithListEditor> {}

  /**
   * See <a href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6016" >issue 6016</a>
   */
  static class EditorWithGenericSubEditors implements Editor<Department> {

    PersonGenericEditor<Manager> manager = new PersonGenericEditor<Manager>();

    PersonGenericEditor<Intern> intern = new PersonGenericEditor<Intern>();
  }

  @IsDriver
  interface EditorWithGenericSubEditorsDriver
      extends SimpleBeanEditorDriver<Department, EditorWithGenericSubEditors> {}

  class LeafAddressEditor extends AddressEditor implements LeafValueEditor<Address> {
    /*
     * These two fields are used to ensure that getValue() and setValue() aren't
     * called excessively.
     */
    int getValueCalled;
    int setValueCalled;
    Address value;

    @Override
    public Address getValue() {
      getValueCalled++;
      return value;
    }

    @Override
    public void setValue(Address value) {
      setValueCalled++;
      value = new Address();
    }
  }

  @IsDriver
  interface ListEditorDriver
      extends SimpleBeanEditorDriver<List<String>, ListEditor<String, SimpleEditor<String>>> {}

  class PersonEditorWithAddressEditorView implements Editor<Person> {
    AddressEditorView addressEditor = new AddressEditorView();
    SimpleEditor<String> name = SimpleEditor.of(UNINITIALIZED);
  }

  @IsDriver
  interface PersonEditorWithAddressEditorViewDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithAddressEditorView> {}

  class SampleView implements Editor<Sample> {
    SimpleEditor<Integer> id = SimpleEditor.of(0);
  }

  @IsDriver
  interface SampleEditorDriver extends SimpleBeanEditorDriver<Sample, SampleView> {}

  /** A test for assigning the object associated with an editor to an immediate child editors. */
  class PersonEditorWithAliasedSubEditors implements Editor<Person> {
    @Path("")
    PersonEditor e1 = new PersonEditor();

    @Path("")
    PersonEditor e2 = new PersonEditor();
  }

  @IsDriver
  interface PersonEditorWithAliasedSubEditorsDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithAliasedSubEditors> {}

  class PersonEditorWithCoAddressEditorView implements Editor<Person> {
    AddressCoEditorView addressEditor = new AddressCoEditorView();
    SimpleEditor<String> name = SimpleEditor.of(UNINITIALIZED);
  }

  @IsDriver
  interface PersonEditorWithCoAddressEditorViewDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithCoAddressEditorView> {}

  static class PersonEditorWithDelegate extends PersonEditor implements HasEditorDelegate<Person> {
    EditorDelegate<Person> delegate;

    @Override
    public void setDelegate(EditorDelegate<Person> delegate) {
      this.delegate = delegate;
    }
  }

  @IsDriver
  interface PersonEditorWithDelegateDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithDelegate> {}

  class PersonEditorWithLeafAddressEditor implements Editor<Person> {
    LeafAddressEditor addressEditor = new LeafAddressEditor();
    SimpleEditor<String> name = SimpleEditor.of(UNINITIALIZED);

    @Path("manager.name")
    SimpleEditor<String> managerName = SimpleEditor.of(UNINITIALIZED);
  }

  @IsDriver
  interface PersonEditorWithLeafAddressEditorDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithLeafAddressEditor> {}

  class PersonEditorWithManagerNameWithDelegate implements Editor<Person> {
    @Path("manager.name")
    SimpleEditorWithDelegate<String> managerName =
        new SimpleEditorWithDelegate<String>(UNINITIALIZED);
  }

  @IsDriver
  interface PersonEditorWithManagerNameWithDelegateDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithManagerNameWithDelegate> {}

  class PersonEditorWithMultipleBindings implements Editor<Person> {
    @Editor.Path("address")
    AddressEditorPartOne one = new AddressEditorPartOne();

    @Editor.Path("address")
    AddressEditorPartTwo two = new AddressEditorPartTwo();
  }

  @IsDriver
  interface PersonEditorWithMultipleBindingsDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithMultipleBindings> {}

  @IsDriver
  interface PersonEditorWithOptionalAddressDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithOptionalAddressEditor> {}

  static class PersonEditorWithOptionalAddressEditor implements Editor<Person> {
    OptionalFieldEditor<Address, AddressEditor> address;
    SimpleEditor<String> name = SimpleEditor.of(UNINITIALIZED);

    public PersonEditorWithOptionalAddressEditor(AddressEditor delegate) {
      address = OptionalFieldEditor.of(delegate);
    }
  }

  class PersonEditorWithValueAwareAddressEditor implements Editor<Person> {
    ValueAwareAddressEditor addressEditor = new ValueAwareAddressEditor();
    SimpleEditor<String> name = SimpleEditor.of(UNINITIALIZED);

    @Path("manager.name")
    SimpleEditor<String> managerName = SimpleEditor.of(UNINITIALIZED);
  }

  @IsDriver
  interface PersonEditorWithValueAwareAddressEditorDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithValueAwareAddressEditor> {}

  class PersonEditorWithValueAwareLeafAddressEditor implements Editor<Person> {
    ValueAwareLeafAddressEditor addressEditor = new ValueAwareLeafAddressEditor();
    SimpleEditor<String> name = SimpleEditor.of(UNINITIALIZED);

    @Path("manager.name")
    SimpleEditor<String> managerName = SimpleEditor.of(UNINITIALIZED);
  }

  @IsDriver
  interface PersonEditorWithValueAwareLeafAddressEditorDriver
      extends SimpleBeanEditorDriver<Person, PersonEditorWithValueAwareLeafAddressEditor> {}

  static class PersonGenericEditor<T extends Person> implements Editor<T> {
    SimpleEditor<String> name = SimpleEditor.of(UNINITIALIZED);
  }

  class PersonWithList {
    String name = "PersonWithList";
    List<Address> addresses = new ArrayList<Address>();

    public List<Address> getAddresses() {
      return addresses;
    }

    public String getName() {
      return name;
    }
  }

  class PersonWithListEditor implements Editor<PersonWithList> {
    SimpleEditor<String> nameEditor = SimpleEditor.of(UNINITIALIZED);
    ListEditor<Address, ValueAwareAddressEditor> addressesEditor =
        ListEditor.of(
            new EditorSource<ValueAwareAddressEditor>() {
              @Override
              public ValueAwareAddressEditor create(int index) {
                return new ValueAwareAddressEditor();
              }
            });
  }

  @IsDriver
  interface PersonWithListEditorDriver
      extends SimpleBeanEditorDriver<PersonWithList, PersonWithListEditor> {}

  class SimpleEditorWithDelegate<T> extends SimpleEditor<T> implements HasEditorDelegate<T> {
    EditorDelegate<T> delegate;

    public SimpleEditorWithDelegate(T value) {
      super(value);
    }

    @Override
    public void setDelegate(EditorDelegate<T> delegate) {
      this.delegate = delegate;
    }
  }

  class ValueAwareAddressEditor extends AddressEditor implements ValueAwareEditor<Address> {
    int flushCalled;
    int setDelegateCalled;
    int setValueCalled;
    Address value;

    @Override
    public void flush() {
      flushCalled++;
    }

    @Override
    public void onPropertyChange(String... paths) {}

    @Override
    public void setDelegate(EditorDelegate<Address> delegate) {
      setDelegateCalled++;
    }

    @Override
    public void setValue(Address value) {
      setValueCalled++;
      this.value = value;
    }
  }

  /** All the mix-ins. Not that this seems like a good idea... */
  class ValueAwareLeafAddressEditor extends LeafAddressEditor implements ValueAwareEditor<Address> {
    int flushCalled;
    int setDelegateCalled;

    @Override
    public void flush() {
      flushCalled++;
    }

    @Override
    public void onPropertyChange(String... paths) {}

    @Override
    public void setDelegate(EditorDelegate<Address> delegate) {
      setDelegateCalled++;
    }
  }

  class TaggedItemAddressEditor implements Editor<TaggedItem<Address>> {
    @Path("item.city")
    SimpleEditor<String> itemCityEditor = SimpleEditor.of(UNINITIALIZED);

    @Path("item.street")
    SimpleEditor<String> itemStreetEditor = SimpleEditor.of(UNINITIALIZED);

    @Path("tag")
    SimpleEditor<String> tagEditor = SimpleEditor.of(UNINITIALIZED);
  }

  @IsDriver
  interface TaggedItemAddressEditorDriver
      extends SimpleBeanEditorDriver<TaggedItem<Address>, TaggedItemAddressEditor> {}

  Person person;
  Address personAddress;
  Person manager;
  long now;

  static final String UNINITIALIZED = "uninitialized";

  @Test
  public void test() {
    PersonEditorDriver driver = new PersonEditorDriver_Impl();
    PersonEditor editor = new PersonEditor();
    driver.initialize(editor);
    driver.edit(person);
    assertEquals(now, editor.localTime.getValue().longValue());
    assertEquals("Alice", editor.name.getValue());
    assertEquals("City", editor.addressEditor.city.getValue());
    assertEquals("Street", editor.addressEditor.street.getValue());
    assertEquals("Bill", editor.managerName.getValue());

    editor.localTime.setValue(now + 1);
    editor.name.setValue("Charles");
    editor.addressEditor.city.setValue("Wootville");
    editor.addressEditor.street.setValue("12345");
    editor.managerName.setValue("David");

    driver.flush();
    assertEquals(now + 1, person.localTime);
    assertEquals("Charles", person.name);
    assertEquals("Wootville", person.address.city);
    assertEquals("12345", person.address.street);
    assertEquals("David", person.manager.name);
  }

  @Test
  public void testAliasedEditors() {
    PersonEditorWithAliasedSubEditors editor = new PersonEditorWithAliasedSubEditors();
    PersonEditorWithAliasedSubEditorsDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithAliasedSubEditorsDriver_Impl();
    driver.initialize(editor);
    driver.edit(person);

    assertEquals("Alice", editor.e1.name.getValue());
    assertEquals("Alice", editor.e2.name.getValue());

    /*
     * Expecting that aliased editors will be editing disjoint sets of
     * properties, but we want to at least have a predictable behavior if two
     * editors are assigned to the same property.
     */
    editor.e1.name.setValue("Should not see this");
    driver.flush();
    assertEquals("Alice", person.getName());

    editor.e2.name.setValue("Should see this");
    driver.flush();
    assertEquals("Should see this", person.getName());
  }

  @Test
  public void testDelegatePath() {
    PersonEditorWithManagerNameWithDelegate editor = new PersonEditorWithManagerNameWithDelegate();
    PersonEditorWithManagerNameWithDelegateDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithManagerNameWithDelegateDriver_Impl();
    driver.initialize(editor);
    driver.edit(person);

    assertEquals("manager.name", editor.managerName.delegate.getPath());
  }

  /**
   * See <a href="https://code.google.com/p/google-web-toolkit/issues/detail?id=6139" >issue
   * 6139</a>
   */
  @Test
  public void testEditorOfSuperclass() {
    DepartmentWithList dpt = new DepartmentWithList();
    Manager manager = new Manager();
    manager.name = "manager";
    dpt.setManager(manager);
    Intern intern = new Intern();
    intern.name = "intern";
    dpt.interns.add(intern);

    DepartmentWithListEditor editor = new DepartmentWithListEditor();
    DepartmentWithListEditorDriver driver =
        new SimpleBeanEditorTest_DepartmentWithListEditorDriver_Impl();
    driver.initialize(editor);

    driver.edit(dpt);

    assertEquals("manager", editor.manager.name.getValue());
    assertEquals("intern", editor.interns.getEditors().get(0).name.getValue());

    editor.manager.name.setValue("MANAGER");
    editor.interns.getEditors().get(0).name.setValue("INTERN");

    driver.flush();

    assertEquals("MANAGER", manager.name);
    assertEquals("INTERN", intern.name);
  }

  /**
   * See <a href="http://code.google.com/p/google-web-toolkit/issues/detail?id=6016" >issue 6016</a>
   */
  @Test
  public void testEditorWithGenericSubEditors() {
    EditorWithGenericSubEditorsDriver driver =
        new SimpleBeanEditorTest_EditorWithGenericSubEditorsDriver_Impl();
    // Issue 6016 would make the above line fail (when generating the editor
    // driver), but let's try editing an object too, it doesn't cost much.
    driver.initialize(new EditorWithGenericSubEditors());
    driver.edit(new Department());
  }

  @Test
  public void testEditorWithNullSubEditor() {
    PersonEditor editor = new PersonEditor();
    editor.addressEditor = null;
    PersonEditorDriver driver = new PersonEditorDriver_Impl();
    driver.initialize(editor);
    driver.edit(person);

    assertEquals("Alice", editor.name.getValue());
    editor.name.setValue("New name");
    driver.flush();
    assertEquals("New name", person.getName());

    /*
     * Verify that changing the editor structure without re-initializing is a
     * no-op.
     */
    editor.name.setValue("Should see this");
    editor.addressEditor = new AddressEditor();
    editor.addressEditor.city.setValue("Should not see this");
    driver.flush();
    assertEquals("Should see this", person.getName());
    assertEquals("City", person.getAddress().getCity());

    // Re-initialize and check that flushing works again
    driver.initialize(editor);
    driver.edit(person);
    editor.addressEditor.city.setValue("Should see this");
    driver.flush();
    assertEquals("Should see this", person.getAddress().getCity());
  }

  /** Test the use of the IsEditor interface that allows a view object to encapsulate its Editor. */
  @Test
  public void testIsEditorView() {
    PersonEditorWithAddressEditorView personEditor = new PersonEditorWithAddressEditorView();
    PersonEditorWithAddressEditorViewDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithAddressEditorViewDriver_Impl();
    testLeafAddressEditor(driver, personEditor, personEditor.addressEditor.asEditor(), true);
  }

  /**
   * Test the use of the IsEditor interface that allows a view object to encapsulate its Editor as
   * well as be an editor itself.
   */
  @Test
  public void testIsEditorViewWithCoEditorA() {
    PersonEditorWithCoAddressEditorView personEditor = new PersonEditorWithCoAddressEditorView();
    PersonEditorWithCoAddressEditorViewDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithCoAddressEditorViewDriver_Impl();
    testLeafAddressEditor(driver, personEditor, personEditor.addressEditor, true);
  }

  /**
   * Test the use of the IsEditor interface that allows a view object to encapsulate its Editor as
   * well as be an editor itself.
   */
  @Test
  public void testIsEditorViewWithCoEditorB() {
    PersonEditorWithCoAddressEditorView personEditor = new PersonEditorWithCoAddressEditorView();
    PersonEditorWithCoAddressEditorViewDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithCoAddressEditorViewDriver_Impl();
    testLeafAddressEditor(driver, personEditor, personEditor.addressEditor.asEditor(), true);
  }

  /**
   * We want to verify that the sub-editors of a LeafValueEditor are not initialized. Additonally,
   * we want to ensure that the instance returned from the LVE is assigned into the owner type.
   */
  @Test
  public void testLeafValueEditorDeclaredInSlot() {
    PersonEditorWithLeafAddressEditor personEditor = new PersonEditorWithLeafAddressEditor();
    PersonEditorWithLeafAddressEditorDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithLeafAddressEditorDriver_Impl();
    LeafAddressEditor addressEditor = personEditor.addressEditor;

    testLeafAddressEditor(driver, personEditor, addressEditor, true);
  }

  /**
   * This tests a weird case where the user has put a LeafValueEditor into a plain Editor field.
   * Because the Editor system relies heavily on static type information at compile time, this is
   * not a supported configuration.
   */
  @Test
  public void testLeafValueEditorInPlainEditorSlot() {
    PersonEditorDriver driver = new PersonEditorDriver_Impl();

    PersonEditor personEditor = new PersonEditor();
    LeafAddressEditor addressEditor = new LeafAddressEditor();

    // Runtime assignment of unexpected LeafValueEditor
    personEditor.addressEditor = addressEditor;

    testLeafAddressEditor(driver, personEditor, addressEditor, true);
  }

  @Test
  public void testLifecycle() {
    PersonEditorDriver driver = new PersonEditorDriver_Impl();
    try {
      driver.edit(person);
      fail("Should have thrown execption");
    } catch (IllegalStateException expected) {
    }
    driver.initialize(new PersonEditor());
    try {
      driver.flush();
      fail("Should have thrown exception");
    } catch (IllegalStateException expected) {
    }
    driver.edit(person);
    driver.flush();
  }

  /** Test a top-level ListEditor. */
  @Test
  public void testListEditor() {
    final SortedMap<Integer, SimpleEditor<String>> positionMap =
        new TreeMap<Integer, SimpleEditor<String>>();
    @SuppressWarnings("unchecked")
    final SimpleEditor<String>[] disposed = new SimpleEditor[1];
    class StringSource extends EditorSource<SimpleEditor<String>> {
      @Override
      public SimpleEditor<String> create(int index) {
        SimpleEditor<String> editor = SimpleEditor.of();
        positionMap.put(index, editor);
        return editor;
      }

      @Override
      public void dispose(SimpleEditor<String> editor) {
        disposed[0] = editor;
        positionMap.values().remove(editor);
      }

      @Override
      public void setIndex(SimpleEditor<String> editor, int index) {
        positionMap.values().remove(editor);
        positionMap.put(index, editor);
      }
    };

    ListEditorDriver driver = new SimpleBeanEditorTest_ListEditorDriver_Impl();
    ListEditor<String, SimpleEditor<String>> editor = ListEditor.of(new StringSource());

    driver.initialize(editor);

    List<String> rawData = new ArrayList<String>(Arrays.asList("foo", "bar", "baz"));
    driver.edit(rawData);

    List<SimpleEditor<String>> editors = editor.getEditors();
    assertEquals(rawData.size(), editors.size());
    assertEquals(
        rawData,
        Arrays.asList(
            editors.get(0).getValue(), editors.get(1).getValue(), editors.get(2).getValue()));
    assertEquals(editors, new ArrayList<SimpleEditor<String>>(positionMap.values()));

    List<String> mutableList = editor.getList();
    assertEquals(rawData, mutableList);

    // Test through wrapped list
    mutableList.set(1, "Hello");
    assertEquals("Hello", editors.get(1).getValue());

    // Test using editor
    editors.get(2).setValue("World");
    assertEquals("baz", rawData.get(2));
    driver.flush();
    assertEquals("World", rawData.get(2));

    // Change list size
    mutableList.add("quux");
    assertEquals(4, editors.size());
    assertEquals("quux", editors.get(3).getValue());
    assertEquals(editors, new ArrayList<SimpleEditor<String>>(positionMap.values()));

    // Delete an element
    SimpleEditor<String> expectedDisposed = editors.get(0);
    mutableList.remove(0);
    assertSame(expectedDisposed, disposed[0]);
    assertEquals(3, editors.size());
    assertEquals("quux", editors.get(2).getValue());
    assertEquals(editors, new ArrayList<SimpleEditor<String>>(positionMap.values()));

    // Change list outside editor: shouldn't impact editors
    rawData.clear();
    rawData.addAll(Arrays.asList("able", "baker"));
    List<String> expectedList = Arrays.asList("Hello", "World", "quux");
    assertEquals(expectedList, editor.getList());
    assertEquals(expectedList.size(), editors.size());
    assertEquals(
        expectedList,
        Arrays.asList(
            editors.get(0).getValue(), editors.get(1).getValue(), editors.get(2).getValue()));
    assertEquals(editors, new ArrayList<SimpleEditor<String>>(positionMap.values()));

    // Edit again: should reuse sub-editors and dispose unneeded ones
    disposed[0] = null;
    expectedDisposed = editors.get(2);
    @SuppressWarnings("unchecked")
    List<SimpleEditor<String>> expectedEditors = Arrays.asList(editors.get(0), editors.get(1));
    driver.edit(rawData);
    assertEquals(expectedEditors, editors);
    assertEquals(expectedEditors, editor.getEditors());
    assertEquals(rawData, editor.getList());
    assertEquals(rawData.size(), editors.size());
    assertEquals(rawData, Arrays.asList(editors.get(0).getValue(), editors.get(1).getValue()));
    assertEquals(editors, new ArrayList<SimpleEditor<String>>(positionMap.values()));
    assertEquals(expectedDisposed, disposed[0]);
  }

  /** Ensure that a ListEditor deeper in the chain is properly flushed. */
  @Test
  public void testListEditorChainFlush() {
    PersonWithListEditorDriver driver = new SimpleBeanEditorTest_PersonWithListEditorDriver_Impl();
    PersonWithList person = new PersonWithList();
    Address a1 = new Address();
    a1.city = "a1City";
    a1.street = "a1Street";
    Address a2 = new Address();
    a2.city = "a2City";
    a2.street = "a2Street";

    person.addresses.add(a1);
    person.addresses.add(a2);

    PersonWithListEditor personEditor = new PersonWithListEditor();
    // Initialize
    driver.initialize(personEditor);
    // Address editors aren't created until edit() is called
    assertEquals(Collections.emptyList(), personEditor.addressesEditor.getEditors());

    // Edit
    driver.edit(person);
    ValueAwareAddressEditor addressEditor = personEditor.addressesEditor.getEditors().get(1);
    // Check that setValue is only called once on sub-editors (issue 7038)
    assertEquals(1, addressEditor.setValueCalled);

    assertEquals("a2City", addressEditor.city.getValue());
    addressEditor.city.setValue("edited");

    // Flush
    driver.flush();
    assertEquals("edited", person.addresses.get(1).getCity());

    // Verify that setting the same list reuses sub-editors
    addressEditor.setValueCalled = 0;
    driver.edit(person);
    assertSame(addressEditor, personEditor.addressesEditor.getEditors().get(1));
    // Check that setValue has correctly been called on the sub-editor anyway
    assertEquals(1, addressEditor.setValueCalled);

    // Edit with a null list
    person.addresses = null;
    driver.edit(person);
    assertNull(personEditor.addressesEditor.getList());
    assertEquals(Collections.emptyList(), personEditor.addressesEditor.getEditors());
    // Flushing should not throw:
    driver.flush();
    assertNull(person.addresses);
  }

  @Test
  public void testMultipleBinding() {
    PersonEditorWithMultipleBindingsDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithMultipleBindingsDriver_Impl();
    PersonEditorWithMultipleBindings editor = new PersonEditorWithMultipleBindings();

    driver.initialize(editor);
    driver.edit(person);
    assertEquals("City", editor.one.city.getValue());
    assertEquals("Street", editor.two.street.getValue());

    editor.one.city.setValue("Foo");
    editor.two.street.setValue("Bar");
    driver.flush();

    assertEquals("Foo", person.getAddress().getCity());
    assertEquals("Bar", person.getAddress().getStreet());
  }

  @Test
  public void testOptionalField() {
    PersonEditorWithOptionalAddressDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithOptionalAddressDriver_Impl();
    person.address = null;

    AddressEditor delegate = new AddressEditor();
    PersonEditorWithOptionalAddressEditor editor =
        new PersonEditorWithOptionalAddressEditor(delegate);
    driver.initialize(editor);

    // Make sure we don't blow up with the null field
    driver.edit(person);
    editor.address.setValue(personAddress);
    assertEquals("City", delegate.city.getValue());
    delegate.city.setValue("Hello");
    driver.flush();
    assertNotNull(person.address);
    assertSame(personAddress, person.address);
    assertEquals("Hello", personAddress.city);

    editor.address.setValue(null);
    driver.flush();
    assertNull(person.address);
  }

  @Test
  public void testValueAwareEditorInDeclaredSlot() {
    PersonEditorWithValueAwareAddressEditorDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithValueAwareAddressEditorDriver_Impl();
    PersonEditorWithValueAwareAddressEditor personEditor =
        new PersonEditorWithValueAwareAddressEditor();
    ValueAwareAddressEditor addressEditor = personEditor.addressEditor;

    testValueAwareAddressEditor(driver, personEditor, addressEditor);
  }

  /** This is another instance of a LeafValueEditor found in a plain slot. */
  @Test
  public void testValueAwareEditorInPlainSlot() {
    PersonEditorDriver driver = new PersonEditorDriver_Impl();

    PersonEditor personEditor = new PersonEditor();
    ValueAwareAddressEditor addressEditor = new ValueAwareAddressEditor();

    // Runtime assignment of unexpected LeafValueEditor
    personEditor.addressEditor = addressEditor;

    testValueAwareAddressEditor(driver, personEditor, addressEditor);
  }

  @Test
  public void testValueAwareLeafValueEditorInDeclaredSlot() {
    PersonEditorWithValueAwareLeafAddressEditor personEditor =
        new PersonEditorWithValueAwareLeafAddressEditor();
    PersonEditorWithValueAwareLeafAddressEditorDriver driver =
        new SimpleBeanEditorTest_PersonEditorWithValueAwareLeafAddressEditorDriver_Impl();
    ValueAwareLeafAddressEditor addressEditor = personEditor.addressEditor;

    testLeafAddressEditor(driver, personEditor, addressEditor, true);
    assertEquals(1, addressEditor.flushCalled);
    assertEquals(1, addressEditor.setDelegateCalled);
  }

  @Test
  public void testValueAwareLeafValueEditorInPlainEditorSlot() {
    PersonEditorDriver driver = new PersonEditorDriver_Impl();

    PersonEditor personEditor = new PersonEditor();
    ValueAwareLeafAddressEditor addressEditor = new ValueAwareLeafAddressEditor();

    // Runtime assignment of unexpected LeafValueEditor
    personEditor.addressEditor = addressEditor;

    testLeafAddressEditor(driver, personEditor, addressEditor, true);
    assertEquals(1, addressEditor.flushCalled);
    assertEquals(1, addressEditor.setDelegateCalled);
  }

  @Test
  public void testEditorWithParameterizedModel() {
    TaggedItemAddressEditorDriver driver =
        new SimpleBeanEditorTest_TaggedItemAddressEditorDriver_Impl();
    TaggedItemAddressEditor editor = new TaggedItemAddressEditor();
    driver.initialize(editor);

    TaggedItem<Address> taggedAddress = new TaggedItem<Address>();
    taggedAddress.setTag("tag1");
    taggedAddress.setItem(personAddress);
    driver.edit(taggedAddress);

    assertEquals("tag1", editor.tagEditor.getValue());
    assertEquals("City", editor.itemCityEditor.getValue());
    assertEquals("Street", editor.itemStreetEditor.getValue());

    editor.tagEditor.setValue("tag2");
    editor.itemCityEditor.setValue("Town");
    editor.itemStreetEditor.setValue("Road");
    driver.flush();

    assertEquals("tag2", taggedAddress.getTag());
    assertEquals("Town", taggedAddress.getItem().getCity());
    assertEquals("Road", taggedAddress.getItem().getStreet());
  }

  {
    personAddress = new Address();
    personAddress.city = "City";
    personAddress.street = "Street";

    manager = new Person();
    manager.name = "Bill";

    person = new Person();
    person.address = personAddress;
    person.name = "Alice";
    person.manager = manager;
    person.localTime = now = System.currentTimeMillis();
  }

  private <T extends Editor<Person>> void testLeafAddressEditor(
      SimpleBeanEditorDriver<Person, T> driver,
      T personEditor,
      LeafAddressEditor addressEditor,
      boolean declaredAsLeaf) {
    Address oldAddress = person.address;
    // Initialize
    driver.initialize(personEditor);
    assertEquals(0, addressEditor.setValueCalled);
    assertEquals(0, addressEditor.getValueCalled);

    // Edit
    driver.edit(person);
    assertEquals(1, addressEditor.setValueCalled);
    // The DirtCollector will interrogate the leaf editors
    assertEquals(1, addressEditor.getValueCalled);

    // Flush
    driver.flush();
    assertEquals(1, addressEditor.setValueCalled);
    assertEquals(2, addressEditor.getValueCalled);
    assertNotSame(oldAddress, person.address);
    assertSame(person.address, addressEditor.value);
  }

  private <T extends Editor<Person>> void testValueAwareAddressEditor(
      SimpleBeanEditorDriver<Person, T> driver,
      T personEditor,
      ValueAwareAddressEditor addressEditor) {
    Address oldAddress = person.address;
    // Initialize
    driver.initialize(personEditor);
    assertEquals(0, addressEditor.setValueCalled);
    assertEquals(0, addressEditor.setDelegateCalled);
    assertEquals(0, addressEditor.flushCalled);

    // Edit
    driver.edit(person);
    assertEquals(1, addressEditor.setValueCalled);
    assertEquals(1, addressEditor.setDelegateCalled);
    assertEquals(0, addressEditor.flushCalled);
    assertEquals("City", addressEditor.city.getValue());

    // Flush
    driver.flush();
    assertEquals(1, addressEditor.setValueCalled);
    assertEquals(1, addressEditor.setDelegateCalled);
    assertEquals(1, addressEditor.flushCalled);
    assertSame(oldAddress, person.address);
    assertSame(person.address, addressEditor.value);
  }
}
